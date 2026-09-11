package hk.org.ha.iams.termsearch.exception;

public final class IamsExceptionHelper {

    private IamsExceptionHelper() {
    }

    public static void throwsIAMSException(String message, Exception cause) {
        throw new IamsDataAccessException(message, cause);
    }
}
