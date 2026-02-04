package io.github.mjbarnette.pokemon.databse;

import java.sql.SQLException;
import java.util.List;


public interface PokemonDao {
    
    void save(Pokemon newPokemon) throws SQLException;
    Pokemon findByName(String name) throws SQLException;
    List<Pokemon> getAllPokemon(SortOrder orderBy) throws SQLException;
    List<Pokemon> getAllPokemonOfType(PokemonTypes type) throws SQLException;    
    void setEvolution(Pokemon current, Pokemon nextStage) throws SQLException;
    List<Pokemon> getEvolution(String name) throws SQLException;
    void deletePokemon(String name) throws SQLException;
    
}
