package io.github.mjbarnette.pokemon.databse;

public class PokemonMove {

    private String name;
    private PokemonTypes type; //Enum
    private int energyTypeCost;
    private int energyGenericCost;
    private int damage;
    private String description;
    
    public PokemonMove(String name, PokemonTypes type, int energyTypeCost, int energyGenericCost, int damge, String description)
    {
        this.name = name;
        this.type = type;
        this.energyTypeCost = energyTypeCost;
        this.energyGenericCost = energyGenericCost;
        this.damage = damage;
        this.description = description;
    }
    
    public String getName() {
    return name;
    }

    public PokemonTypes getType() {
        return type;
    }

    public int getEnergyTypeCost() {
        return energyTypeCost;
    }

    public int getEnergyGenericCost() {
        return energyGenericCost;
    }

    public int getDamage() {
        return damage;
    }

    public String getDescription() {
        return description;
    }

}
