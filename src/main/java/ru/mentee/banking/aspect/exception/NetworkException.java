/* @MENTEE_POWER (C)2025 */
package ru.mentee.banking.aspect.exception;

public class NetworkException extends RuntimeException {
    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
