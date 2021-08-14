package me.lorenzo0111.rocketplaceholders.exceptions;

import java.io.IOException;

public class InvalidResponseException extends IOException {

    public InvalidResponseException(String message) {
        super("An error has occurred while reading editor response: " + message);
    }

    public InvalidResponseException(Throwable cause) {
        super("An error has occurred while reading editor response", cause);
    }
}
