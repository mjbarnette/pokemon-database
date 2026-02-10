package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.entity.Moves;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class MoveDTO {
    
    private final String name;   
    private final int damage;
    private final String description;
    @Getter(AccessLevel.NONE)
    private final Map<PokemonTypes, Integer> energyCosts;
    
    
    public MoveDTO(
            String name, 
            int damage, 
            String description, 
            Map<PokemonTypes, Integer> costs)
    {
        this.name = name;
        this.damage = damage;
        this.description = description;
        this.energyCosts = new HashMap<>(costs);        
    }
    
    public static MoveDTO fromEntity(Moves move)
    {
        HashMap<PokemonTypes, Integer> costs = new HashMap<>();
        Set<PokemonTypes> types = move.getEnergyTypes();
        for(PokemonTypes pT : types)
        {
            costs.put(pT, move.getEnergyTypeCost(pT));
        }
        return new MoveDTO(
                move.getName(),
                move.getDamage(),
                move.getDescription(),
                costs        
        );
    }  
   
    public Set<PokemonTypes> getEnergyTypes()
    {
        return Collections.unmodifiableSet(energyCosts.keySet());
    }
    
    public int getCost(PokemonTypes type)
    {
        return this.energyCosts.get(type);
    }
}
