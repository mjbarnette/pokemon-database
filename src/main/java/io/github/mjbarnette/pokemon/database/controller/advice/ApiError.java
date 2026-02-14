

package io.github.mjbarnette.pokemon.database.controller.advice;

import java.time.Instant;
import lombok.Getter;

@Getter
public class ApiError {

    private final String errorCode;
    private final String message;
    private final Instant timestamp;
    
    public ApiError(String errorCode, String message, Instant timestamp)
    {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }
    
}
