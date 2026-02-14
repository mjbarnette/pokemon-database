
package io.github.mjbarnette.pokemon.database.service;

import io.github.mjbarnette.pokemon.database.value.PokemonErrorCode;


public class PokemonDomainException extends RuntimeException {

    private final PokemonErrorCode status;
    
    
    public PokemonDomainException(PokemonErrorCode status, String message) {
        super(message);
        this.status = status;
    }    
    
    public PokemonErrorCode status()
    {
        return status;
    }
    
}
