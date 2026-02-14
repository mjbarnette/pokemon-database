

package io.github.mjbarnette.pokemon.database.controller.advice;

import io.github.mjbarnette.pokemon.database.service.PokemonDomainException;
import io.github.mjbarnette.pokemon.database.value.PokemonErrorCode;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(PokemonDomainException.class)
    public ResponseEntity<ApiError> handlePokemonDomainException(
            PokemonDomainException ex) {

        HttpStatus status = mapToHttpStatus(ex.status());
        
        ApiError body = new ApiError(ex.status().name(),ex.getMessage(), Instant.now()) {};

        return ResponseEntity.status(status).body(body);
    }
    
    private HttpStatus mapToHttpStatus(PokemonErrorCode status) {
        return switch (status) {
            case POKEMON_NOT_FOUND,
                 CURRENT_POKEMON_NOT_FOUND,
                 NEXT_POKEMON_NOT_FOUND -> HttpStatus.NOT_FOUND;

            case DUPLICATE_POKEMON,
                 DUPLICATE_MOVE,                 
                 DUPLICATE_EVOLUTION -> HttpStatus.CONFLICT;

            default -> HttpStatus.BAD_REQUEST;
        };
    }

}

