package spring.estiloAlPaso.business.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.agencia.AgenciaCreateRequestDto;
import spring.estiloAlPaso.business.api.dto.agencia.AgenciaResponseDto;
import spring.estiloAlPaso.business.api.exeptions.BusinessException;
import spring.estiloAlPaso.business.data.entity.Agencia;
import spring.estiloAlPaso.business.data.repository.AgenciaRepository;
import spring.estiloAlPaso.business.domain.mapper.AgenciaMapper;
import spring.estiloAlPaso.business.domain.service.AgenciaService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AgenciaServiceImpl implements AgenciaService {

    private final AgenciaRepository agenciaRepository;
    private final AgenciaMapper agenciaMapper;

    @Override
    public Page<AgenciaResponseDto> buscarAgencias(String search, Pageable pageable) {

        Page<Agencia> page;

        if (search == null || search.isBlank()) {
            page = agenciaRepository.findAll(pageable);
        } else {
            page = agenciaRepository.buscarPorNombre(search.trim(), pageable);
        }

        return page.map(agenciaMapper::toResponse);
    }

    @Override
    public List<AgenciaResponseDto> autocompleteAgencias(String search) {

        if (search == null || search.isBlank()) {
            return List.of();
        }

        return agenciaRepository
                .findTop10ByNombreContainingIgnoreCaseOrderByNombreAsc(search.trim())
                .stream()
                .map(agenciaMapper::toResponse)
                .toList();
    }

    @Transactional
    public void crearAgencia(AgenciaCreateRequestDto request) {

        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new BusinessException("El nombre de la agencia es obligatorio");
        }

        boolean existe = agenciaRepository
                .existsByNombreIgnoreCase(request.nombre().trim());

        if (existe) {
            throw new BusinessException("La agencia ya existe");
        }

        Agencia agencia = Agencia.builder()
                .nombre(request.nombre().trim())
                .build();

        agenciaRepository.save(agencia);
    }

    @Transactional
    public void importarAgenciasMaestras(InputStream file) {

        try (Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Hoja2");

            if (sheet == null) {
                throw new BusinessException("No se encontró la hoja de agencias");
            }

            Set<String> existentes = agenciaRepository.findAll()
                    .stream()
                    .map(a -> a.getNombre().trim().toUpperCase())
                    .collect(Collectors.toSet());

            List<Agencia> nuevas = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                String nombre = extraerNombre(row.getCell(2));

                if (nombre == null) continue;

                if (existentes.contains(nombre)) continue;

                existentes.add(nombre);

                nuevas.add(Agencia.builder()
                        .nombre(nombre)
                        .build());
            }

            agenciaRepository.saveAll(nuevas);

        } catch (Exception e) {
            throw new BusinessException("Error importando agencias");
        }
    }

    @Transactional
    public void actualizarAgenciasDesdeExcel(InputStream file) {

        try (Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Hoja2");

            if (sheet == null) {
                throw new BusinessException("No se encontró la hoja de agencias");
            }

            Set<String> existentes = agenciaRepository.findAll()
                    .stream()
                    .map(a -> a.getNombre().trim().toUpperCase())
                    .collect(Collectors.toSet());

            List<Agencia> nuevas = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                String nombre = extraerNombre(row.getCell(2));

                if (nombre == null) continue;

                if (existentes.add(nombre)) {
                    nuevas.add(Agencia.builder()
                            .nombre(nombre)
                            .build());
                }
            }

            agenciaRepository.saveAll(nuevas);

        } catch (Exception e) {
            throw new BusinessException("Error actualizando agencias desde Excel");
        }
    }

    private String extraerNombre(Cell cell) {

        if (cell == null) return null;

        DataFormatter formatter = new DataFormatter();
        String nombre = formatter.formatCellValue(cell);

        if (nombre == null) return null;

        nombre = nombre.trim().toUpperCase();

        return nombre.isBlank() ? null : nombre;
    }
}
