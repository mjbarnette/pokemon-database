package io.github.mjbarnette.pokemon.database.entity;

import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Entity
public class Moves {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
        
    @ManyToOne
    @Getter
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;
        
    @Getter
    private String name;
        
    @Getter
    private int damage;
        
    @Getter
    private String description;
    
    @ElementCollection
    @CollectionTable(
        name = "move_energy",
        joinColumns = @JoinColumn(name = "move_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "energy_type")
    @Column(name = "amount")
    @Setter(AccessLevel.PRIVATE)
    private Map<PokemonTypes, Integer> energyCost = new HashMap<>();
    
    protected Moves() {} //JPA only
    
    public Moves(Pokemon pokemon, String name, int damage, String description)
    {
        this.pokemon = pokemon;
        this.name = name;
        this.damage = damage;
        if(description == null)
        {
            description = "";
        }
        this.description = description;
    }    
    
    public Set<PokemonTypes> getEnergyTypes()
    {
        return Collections.unmodifiableSet(energyCost.keySet());        
    }
    
    public int getEnergyTypeCost(PokemonTypes type)
    {
        return energyCost.getOrDefault(type, 0);
    }
    
    public void setEnergyCost(PokemonTypes type, Integer cost)
    {
        this.energyCost.put(type, cost);
    }
    
    @Override
    public String toString()
    {        
        String str = "Title: " + name + "\n" 
                + "Damage: " + damage + "\n"
                + "Description: " + description + "\n";
        Set<PokemonTypes> list = energyCost.keySet();
        for(PokemonTypes type : list)
        {
            str += "EnergyType: " + type + " -> " + energyCost.get(type) + "\n";
        }        
        return str;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof Moves)) 
            return false;
        return id != null && id.equals(((Moves) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }    
    
}
