
package io.github.mjbarnette.pokemon.database.dto;

import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateMoveDTO {
    
    private final String name; 
    private final int damage;
    private final String description;
    @Getter(AccessLevel.NONE)
    private final Map<PokemonTypes, Integer> energyCosts;
    
    public CreateMoveDTO(String name,
            int damage,
            String description,
            Map<PokemonTypes, Integer> energyCosts)
    {
        this.name = name;
        this.damage = damage;
        this.description = description;
        this.energyCosts = new HashMap<>(energyCosts);
    }
    
    public Map<PokemonTypes, Integer> getEnergyCosts()
    {
        return Collections.unmodifiableMap(energyCosts);
    }
    
    public Set<PokemonTypes> getEnergyTypes()
    {
        return Collections.unmodifiableSet(energyCosts.keySet());
    }
    
    public int getTypeCost(PokemonTypes type)
    {
        return energyCosts.get(type);
    }
}
