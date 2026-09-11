package hk.org.ha.iams.termsearch.exception;

public class IamsDataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IamsDataAccessException(String message) {
        super(message);
    }

    public IamsDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
