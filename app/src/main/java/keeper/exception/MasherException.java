package keeper.exception;

public class MasherException extends RuntimeException {
   public MasherException(String message, Throwable cause) {
        super(message, cause);
    }

    public MasherException(Throwable cause) {
        super(cause);
    }

    public MasherException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
