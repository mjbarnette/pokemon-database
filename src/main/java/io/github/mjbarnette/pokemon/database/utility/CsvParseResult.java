

package io.github.mjbarnette.pokemon.database.utility;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.CsvErrorCodes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParseResult {

    private final List<Pokemon> pokemonList;
    private final Map<CsvErrorCodes, List<String>> errorLog;
    
    public CsvParseResult()
    {
        pokemonList = new ArrayList<>();
        errorLog = new HashMap<>();
        errorLog.put(CsvErrorCodes.DUPLICATE_POKEMON_IN_FILE, new ArrayList<String>());
        errorLog.put(CsvErrorCodes.NO_MATCH_MOVE, new ArrayList<String>());
        errorLog.put(CsvErrorCodes.NO_MATCH_EVOLUTION, new ArrayList<String>());
        errorLog.put(CsvErrorCodes.DUPLICATE_POKEMON_IN_TABLE, new ArrayList<String>());
    }
    
    public void addPokemon(Pokemon pokemon)
    {
        pokemonList.add(pokemon);
    }
    
    public void addErrorLog(CsvErrorCodes errorCode, String log)
    {
        List<String> list = errorLog.get(errorCode);
        list.add(log);                      
    }
    
    public Map<CsvErrorCodes, List<String>> getErrorLog()
    {
        return Collections.unmodifiableMap(errorLog);
    }
    
    public List<Pokemon> getPokemonList()
    {
        return Collections.unmodifiableList(pokemonList);
    }
}
