package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePokemonDTO {

    private final String name;
    private final PokemonTypes type;
    private final int hitPoints;
    private final PokemonTypes weakness;
    private final int retreatCost;
    private final EvolutionStage stage;   
    private final String imageFile;
    
}
