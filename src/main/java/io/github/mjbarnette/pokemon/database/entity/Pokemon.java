package io.github.mjbarnette.pokemon.database.entity;

import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Entity
public class Pokemon {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Setter
    private String imagefile;
    
    @OneToOne(mappedBy = "pokemon", cascade = CascadeType.ALL)   
    private Pokestats stats;
    
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Moves> moves = new HashSet<>();
    
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)  
    private Set<Pokeevolutions> evolutions = new HashSet<>();   
    
    protected Pokemon() {} //JPA only
    
    public Pokemon(String name, 
            PokemonTypes type, 
            int hitPoints, 
            PokemonTypes weakness, 
            int retreatCost, 
            EvolutionStage stage)
    {
        this.name = name;
        this.stats = new Pokestats(this, type, hitPoints, weakness, retreatCost);
        Pokeevolutions evolution = new Pokeevolutions(this, stage);
        evolutions.add(evolution);
    }    
    
    public void addMoves(String moveName, int damage, String description, Map<PokemonTypes, Integer> cost)
    {
        Moves newMove = new Moves(this, moveName, damage, description);
        Set<PokemonTypes> types = cost.keySet();
        for(PokemonTypes type : types)
        {
            newMove.setEnergyCost(type, cost.get(type));
        }
        this.moves.add(newMove);
    }
    
    public void addEvolutions(Pokemon next)
    {        
       for(Pokeevolutions evo : evolutions)
       {
           if(evo.getNextPokemon() == null)
           {               
               evo.updateStage(next, next.getCurrentStage());
               return;
           }
       }
       evolutions.add(new Pokeevolutions(this, next));       
    }
    
    public EvolutionStage getCurrentStage()
    {
        Pokeevolutions current = evolutions.iterator().next();
        return current.getCurrentStage();
    }
    
    public Set<Moves> getMoves()
    {
        return Collections.unmodifiableSet(moves);
    }
    
    public Set<Pokeevolutions> getEvolutions()
    {
        return Collections.unmodifiableSet(evolutions);
    }
        
    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof Pokemon)) 
            return false;
        return id != null && id.equals(((Pokemon) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }  
    
    @Override
    public String toString()
    {
        String info = "Name: " + name + "\n";
       
        return info;
    }
}
