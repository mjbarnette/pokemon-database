
package io.github.mjbarnette.pokemon.database.value;


/*
POKEMON_NOT_FOUND - pokemon not in datatable Pokemon
DUPLICATE_MOVE - move.name and pokemon.name are equal to move already in datatable Moves
DUPLICATE_EVOLUTION - both currentPokemon and nextPokemon chain are already in datatable Pokeevolutions
DUPLICATE_POKEMON - pokemon.name already exists in datatable Pokemon
CURRENT_POKEMON_NOT_FOUND - the current pokemon is not found in datatable Pokemon
NEXT_POKEMON_NOT_FOUND - the next pokemon is not found in datatable Pokemon
SUCCESS - action was completed.
*/

public enum PokemonErrorCode {
    
    POKEMON_NOT_FOUND,
    DUPLICATE_MOVE,
    DUPLICATE_EVOLUTION,
    DUPLICATE_POKEMON,
    CURRENT_POKEMON_NOT_FOUND,
    NEXT_POKEMON_NOT_FOUND    
}
