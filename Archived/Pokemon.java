package io.github.mjbarnette.pokemon.databse;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

    private String name;
    private String imageName;
    private PokemonTypes type; //Enum
    private int hitPoints;
    private EvolutionStage stage; //Enum
    private List<PokemonMove> moves;
    private PokemonTypes weakness; //Enum
    private int retreatCost;     
    
    public Pokemon(String name, String imageName, PokemonTypes type, int hitPoints, EvolutionStage stage, PokemonTypes weakness, int retreatCost)
    {
        this.name = name;
        this.imageName = imageName;
        this.type = type;
        this.hitPoints = hitPoints;
        this.stage = stage;
        this.weakness = weakness;
        this.retreatCost = retreatCost;
        moves = new ArrayList<PokemonMove>();
               
    }
    
    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public PokemonTypes getType() {
        return type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public EvolutionStage getStage() {
        return stage;
    }

    public List<PokemonMove> getMoves() {
        return moves;
    }

    public PokemonTypes getWeakness() {
        return weakness;
    }

    public int getRetreatCost() {
        return retreatCost;
    }
    
    public void addMoves(PokemonMove ability)
    {
        moves.add(ability);
    }
    
    public String toString()
    {
        return name + ": " + hitPoints + " Move Count = " + moves.size();
    }

}
