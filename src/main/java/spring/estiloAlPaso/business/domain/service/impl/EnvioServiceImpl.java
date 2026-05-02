package spring.estiloAlPaso.business.domain.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.envio.EnvioListadoResponseDto;
import spring.estiloAlPaso.business.api.dto.envio.EnvioResponseDto;
import spring.estiloAlPaso.business.api.exeptions.BusinessException;
import spring.estiloAlPaso.business.api.exeptions.NotFoundException;
import spring.estiloAlPaso.business.data.entity.Agencia;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;
import spring.estiloAlPaso.business.data.entity.Paquete.EstadoPaquete;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.data.entity.Envio.Envio;
import spring.estiloAlPaso.business.data.entity.Envio.EstadoEnvio;
import spring.estiloAlPaso.business.data.entity.Envio.TipoEnvio;
import spring.estiloAlPaso.business.data.repository.AgenciaRepository;
import spring.estiloAlPaso.business.data.repository.ClienteRepository;
import spring.estiloAlPaso.business.data.repository.EnvioRepository;
import spring.estiloAlPaso.business.data.repository.PaqueteRepository;
import spring.estiloAlPaso.business.domain.mapper.EnvioMapper;
import spring.estiloAlPaso.business.domain.service.EnvioService;
import org.springframework.transaction.annotation.Transactional;
import spring.estiloAlPaso.business.domain.util.EnvioCalculator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private final ClienteRepository clienteRepository;
    private final PaqueteRepository paqueteRepository;
    private final EnvioRepository envioRepository;
    private final AgenciaRepository agenciaRepository;
    private final EnvioMapper envioMapper;

    private final Random random = new Random();

    @Transactional
    @Override
    public void crearEnvio(Integer clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        Paquete paquete = paqueteRepository
                .findByClienteIdAndEstado(clienteId, EstadoPaquete.ACTIVO)
                .orElseThrow(() -> new BusinessException("El cliente no tiene paquete activo"));

        if (paquete.getPrendas().isEmpty()) {
            throw new BusinessException("El paquete no tiene prendas");
        }

        if (envioRepository.existsByPaqueteIdAndEstado(paquete.getId(), EstadoEnvio.PENDIENTE)) {
            throw new BusinessException("Ya existe un envío pendiente para este paquete");
        }

        boolean esTrujillo = cliente.getCiudad() != null
                && cliente.getCiudad().trim().equalsIgnoreCase("trujillo");

        if (esTrujillo && (cliente.getDireccion() == null || cliente.getDireccion().isBlank())) {
            throw new BusinessException("Falta dirección");
        }

        if (!esTrujillo && cliente.getAgencia() == null) {
            throw new BusinessException("Falta agencia");
        }

        Envio envio = new Envio();
        envio.setPaquete(paquete);
        envio.setEstado(EstadoEnvio.PENDIENTE);
        envio.setFechaCreacion(new Date());
        envio.setTipoEnvio(esTrujillo ? TipoEnvio.DELIVERY : TipoEnvio.ENCOMIENDA);

        envio.setCantidadPrendas(paquete.getPrendas().size());
        envio.setTotal(EnvioCalculator.calcularTotal(paquete));
        envio.setPagado(EnvioCalculator.calcularPagado(paquete));

        envioRepository.save(envio);
    }

    @Transactional
    @Override
    public void cancelarEnvio(Integer envioId) {

        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new NotFoundException("Envío no encontrado"));

        if (envio.getEstado() == EstadoEnvio.ENVIADO) {
            throw new BusinessException("No se puede cancelar un envío ya enviado");
        }

        if (envio.getEstado() == EstadoEnvio.CANCELADO) {
            throw new BusinessException("El envío ya está cancelado");
        }

        envio.setEstado(EstadoEnvio.CANCELADO);
    }

    @Transactional
    @Override
    public void marcarComoEnviado(Integer envioId) {

        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new NotFoundException("Envío no encontrado"));

        if (envio.getEstado() != EstadoEnvio.PENDIENTE) {
            throw new BusinessException("Solo se pueden enviar pedidos pendientes");
        }

        if (envio.getClave() == null || envio.getClave().isBlank()) {
            throw new BusinessException("No se puede completar el envío sin clave");
        }

        Paquete paquete = envio.getPaquete();

        BigDecimal total = EnvioCalculator.calcularTotal(paquete);
        BigDecimal pagado = EnvioCalculator.calcularPagado(paquete);

        if (pagado.compareTo(total) < 0) {
            throw new BusinessException("No se puede enviar: el cliente tiene deuda");
        }

        envio.setTotal(total);
        envio.setPagado(pagado);

        envio.setEstado(EstadoEnvio.ENVIADO);
        envio.setFechaEnvio(new Date());

        paquete.setEstado(EstadoPaquete.CERRADO);
    }

    @Override
    public EnvioListadoResponseDto listarEnvios() {

        List<Envio> envios = envioRepository.findAll();

        List<EnvioResponseDto> delivery = new ArrayList<>();
        List<EnvioResponseDto> encomienda = new ArrayList<>();

        for (Envio e : envios) {

            EnvioResponseDto dto = envioMapper.toResponse(e);

            if (e.getTipoEnvio() == TipoEnvio.DELIVERY) {
                delivery.add(dto);
            } else {
                encomienda.add(dto);
            }
        }

        return new EnvioListadoResponseDto(delivery, encomienda);
    }

    @Override
    @Transactional(readOnly = true)
    public EnvioResponseDto obtenerPendiente(Integer clienteId) {

        Envio envio = envioRepository
                .findByPaqueteClienteIdAndEstado(clienteId, EstadoEnvio.PENDIENTE)
                .orElseThrow(() -> new BusinessException("No hay envío pendiente"));

        return envioMapper.toResponse(envio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvioResponseDto> obtenerPorEstado(Integer clienteId, EstadoEnvio estado) {

        List<Envio> envios;

        if (estado != null) {
            envios = envioRepository
                    .findAllByPaqueteClienteIdAndEstado(clienteId, estado);
        } else {
            envios = envioRepository
                    .findAllByPaqueteClienteId(clienteId);
        }

        return envios.stream()
                .map(envioMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public String generarClaveParaEncomiendasPendientes() {

        List<Envio> envios = envioRepository
                .findByTipoEnvioAndEstado(TipoEnvio.ENCOMIENDA, EstadoEnvio.PENDIENTE);

        if (envios.isEmpty()) {
            throw new BusinessException("No hay envíos pendientes para generar clave");
        }

        String clave = generarClaveUnica();

        for (Envio envio : envios) {
            envio.setClave(clave);
        }

        return clave;
    }

    private String generarClaveUnica() {

        String clave;

        do {
            int numero = random.nextInt(9000) + 1000;
            clave = String.valueOf(numero);
        } while (envioRepository.existsByClave(clave));

        return clave;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarExcel(Integer origenId) {

        List<Envio> envios = envioRepository
                .findByTipoEnvioAndEstado(
                        TipoEnvio.ENCOMIENDA,
                        EstadoEnvio.PENDIENTE
                );

        /*if (envios.isEmpty()) {
            throw new BusinessException("No hay envíos pendientes para exportar");
        }*/

        if (!List.of(196, 381).contains(origenId)) {
            throw new BusinessException("Agencia no válida como origen");
        }

        Agencia agenciaOrigen = agenciaRepository.findById(origenId)
                .orElseThrow(() -> new BusinessException("Agencia de origen no existe"));

        String origenNombre = agenciaOrigen.getNombre();

        try (
                InputStream is = getClass().getResourceAsStream("/templates/envios.xlsx");
                Workbook workbook = new XSSFWorkbook(is)
        ) {

            Sheet sheet = workbook.getSheetAt(0);

            int rowIdx = 1;

            for (Envio envio : envios) {

                Cliente c = envio.getPaquete().getCliente();

                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(
                        c.getDni() != null ? c.getDni() : ""
                );

                row.createCell(1).setCellValue(
                        c.getTelefono() != null ? c.getTelefono() : ""
                );

                row.createCell(2).setCellValue(
                        c.getDni() != null ? c.getDni() : ""
                );

                row.createCell(3).setCellValue(
                        c.getTelefono() != null ? c.getTelefono() : ""
                );

                row.createCell(4).setCellValue("");

                row.createCell(5).setCellValue(origenNombre);

                row.createCell(6).setCellValue(
                        c.getAgencia() != null
                                ? c.getAgencia().getNombre()
                                : ""
                );

                row.createCell(7).setCellValue("PAQUETE XXS");
                row.createCell(8).setCellValue(0.1);
                row.createCell(9).setCellValue(0.1);
                row.createCell(10).setCellValue(0.1);
                row.createCell(11).setCellValue(1);
                row.createCell(12).setCellValue(1);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }
}