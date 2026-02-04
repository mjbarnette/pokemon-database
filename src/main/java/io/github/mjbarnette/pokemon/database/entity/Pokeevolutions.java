

package io.github.mjbarnette.pokemon.database.entity;

import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Entity
public class Pokeevolutions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;
    
    @Enumerated(EnumType.STRING)
    private EvolutionStage currentStage;
    
    @OneToOne
    @JoinColumn(name = "nextpoke_id")
    @Setter
    private Pokemon nextPokemon;
    
    @Enumerated(EnumType.STRING)
    @Setter
    private EvolutionStage nextStage; 
    
    protected Pokeevolutions() {} //JPA only
    //JPA requires a no argument constructor, but objects should only be created with Pokemon and current stage set.
    
    public Pokeevolutions(Pokemon pokemon, EvolutionStage currentStage)
    {
        this.pokemon = pokemon;
        this.currentStage = currentStage;
        this.nextPokemon = null;
        this.nextStage = null;
    }
    
    public Pokeevolutions(Pokemon pokemon, Pokemon nextPokemon)
    {
        this.pokemon = pokemon;
        this.currentStage = pokemon.getCurrentStage();
        this.nextPokemon = nextPokemon;
        this.nextStage = nextPokemon.getCurrentStage();
    }
    
    public void updateStage(Pokemon nextPokemon, EvolutionStage nextStage)
    {
        this.nextPokemon = nextPokemon;
        this.nextStage = nextStage;
    }
    
    @Override
    public String toString()
    {
        if(nextPokemon == null)
        {
            return "No known evolution";
        }
        String info = "Next Pokemon: " + this.nextPokemon.getName() + "\n" 
                + "Next Stage: " + this.nextStage + "\n";
        return info;
    }
}
