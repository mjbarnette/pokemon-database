package io.github.mjbarnette.pokemon.databse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PokemonMove {

    private String name;   
    private int damage;
    private String description;
    private Map<PokemonTypes, Integer> energyCost;
    
    public PokemonMove(String name, int damge, String description)
    {
        this.name = name;           
        this.damage = damage;
        this.description = description;
        energyCost = new HashMap<>();
    }
    
    public String getName() {
    return name;
    }
    
    public int getDamage() {
        return damage;
    }

    public String getDescription() {
        return description;
    }
    
    public List<PokemonTypes> getEnergyTypes()
    {
         return List.copyOf(energyCost.keySet());
    }
    
    public int getEnergyCost(PokemonTypes type)
    {
        return energyCost.getOrDefault(type, 0);
    }
    
    public void addEnergyCost(PokemonTypes type, Integer cost)
    {
        this.energyCost.put(type, cost);
    }
    
    public String toString()
    {
        return name + energyCost.keySet();
    }
}
