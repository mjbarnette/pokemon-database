

package io.github.mjbarnette.pokemon.database.entity;

import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Entity
public class Pokestats {
   
    @Id
    @Getter
    private long pokemonId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "pokemon_id")
    @Getter
    private Pokemon pokemon;
    
    @Getter
    @Enumerated(EnumType.STRING)
    private PokemonTypes type; //Enum
    
    @Getter
    private int hitPoints;      
    
    @Getter
    @Enumerated(EnumType.STRING)
    private PokemonTypes weakness; //Enum
    
    @Getter
    private int retreatCost;
    
    protected Pokestats() {} //JPA only
    
    public Pokestats(Pokemon pokemon, PokemonTypes type, int hitPoints, PokemonTypes weakness, int retreatCost)
    {
        this.pokemon = pokemon;        
        this.type = type;
        this.hitPoints = hitPoints;        
        this.weakness = weakness;
        this.retreatCost = retreatCost;        
    }   
   
    @Override
    public String toString()
    {
        String info = "Type: " + type + "\n" 
                + "Hit Points: " + hitPoints + "\n" 
                + "Weakness: " + weakness + "\n"
                + "Retreat Cost: " + retreatCost + "\n";
        return info;
    }
    
}
