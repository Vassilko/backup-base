package ua.od.vassio.backup.common.exception;

/**
 * Created with IntelliJ IDEA.
 * User: vassio
 * Date: 02.08.14
 * Time: 13:21
 */
public class ClearException extends Exception {
    public ClearException() {
    }

    public ClearException(String message) {
        super(message);
    }

    public ClearException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClearException(Throwable cause) {
        super(cause);
    }
}
