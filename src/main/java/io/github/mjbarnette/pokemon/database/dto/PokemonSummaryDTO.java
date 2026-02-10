package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PokemonSummaryDTO {

    private final String name;
    private final PokemonTypes type;
    private final int hitPoints;
    private final EvolutionStage stage;
    private final String imageFile;
   
    
    
    
    public static PokemonSummaryDTO fromEntity(Pokemon pokemon) 
    {
        return new PokemonSummaryDTO(
                pokemon.getName(),
                pokemon.getStats().getType(),
                pokemon.getStats().getHitPoints(),
                pokemon.getCurrentStage(),
                pokemon.getImagefile()
        );
    }   
}
