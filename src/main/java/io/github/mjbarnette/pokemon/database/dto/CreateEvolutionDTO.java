
package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateEvolutionDTO {
    
    private final EvolutionStage currentStage;
    private final String nextPokemonName;
    private final EvolutionStage nextStage;

}
