package spring.estiloAlPaso.business.api.exeptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
