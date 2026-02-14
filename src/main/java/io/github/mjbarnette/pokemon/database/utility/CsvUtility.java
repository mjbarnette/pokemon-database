
package io.github.mjbarnette.pokemon.database.utility;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
import io.github.mjbarnette.pokemon.database.value.CsvErrorCodes;
import io.github.mjbarnette.pokemon.database.value.EvolutionStage;
import io.github.mjbarnette.pokemon.database.value.PokemonTypes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class CsvUtility {
    
    private static final String[] POKEMONHEADERS = {"Name", "Type", "Hit Points", "Weakness", "Retreat Cost", "Stage", "Image File"};
    private static final String[] MOVEHEADERS = {"Pokemon Name", "Title", "damage", "Description", "Energy Types", "Costs"};
    private static final String[] EVOLUTIONHEADERS = {"Current Pokemon", "Next Pokemon"};    
    
    public static CsvParseResult parseCsvFiles(File pokemon, File moves, File evolutions) throws IOException
    {        
        CsvParseResult result = new CsvParseResult();
        parsePokemonCsv(result, pokemon);
        HashMap<String, Pokemon> graph = new HashMap<>();
        for(Pokemon p : result.getPokemonList())
        {            
            graph.put(p.getName(), p);            
        }
        parseMoveCsv(result, graph, moves); 
        parseEvolutions(result, graph, evolutions);
        return result;
    }
    
    private static void parseEvolutions(CsvParseResult result, HashMap<String, Pokemon> graph, File evolutions) throws IOException 
    {            
        InputStream input = new FileInputStream(evolutions);        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));        
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                                                                .setHeader(EVOLUTIONHEADERS)
                                                                .setSkipHeaderRecord(true)
                                                                .build())) {
            for (CSVRecord record : csvParser) {
                String current = record.get(EVOLUTIONHEADERS[0]);
                String next = record.get(EVOLUTIONHEADERS[1]);
                if(!next.equals("End")) //End = end of evolution chain
                {
                    Pokemon pokemonCurrent = graph.get(current);
                    Pokemon pokemonNext = graph.get(next);
                    if(pokemonCurrent != null && pokemonNext != null)
                    {
                        pokemonCurrent.addEvolutions(pokemonNext);
                    }
                    else
                    {
                        result.addErrorLog(CsvErrorCodes.NO_MATCH_EVOLUTION, current + "," + next);
                    }
                }
            }
        }
    }
    
    private static void parseMoveCsv(CsvParseResult result, 
            HashMap<String, Pokemon> graph, 
            File moves) throws IOException
    {       
        InputStream input = new FileInputStream(moves);        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));        
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                                                                .setHeader(MOVEHEADERS)
                                                                .setSkipHeaderRecord(true)
                                                                .build())) {
            for (CSVRecord record : csvParser) {
                String name = record.get(MOVEHEADERS[0]);
                String title = record.get(MOVEHEADERS[1]);
                int damage = Integer.parseInt(record.get(MOVEHEADERS[2]));
                String description = record.get(MOVEHEADERS[3]);
                String energyTypes = record.get(MOVEHEADERS[4]);
                String costs = record.get(MOVEHEADERS[5]);
                String[] types = energyTypes.split(" ");
                String[] amount = costs.split(" ");
                Pokemon pokemon = graph.get(name);  
                HashMap<PokemonTypes, Integer> cost = new HashMap<>();
                if(pokemon != null && types.length == amount.length)
                {
                    for(int i = 0; i < types.length; i++)
                    {
                        cost.put(PokemonTypes.valueOf(types[i]), Integer.parseInt(amount[i]));
                    }               
                    pokemon.addMoves(title, damage, description, cost);
                }
                else
                {
                    String store = (name + "," + title + "," + damage + "," + description + "," + energyTypes + "," + costs);
                    result.addErrorLog(CsvErrorCodes.NO_MATCH_MOVE, store);
                }
            }
        }         
    }
    
    private static void parsePokemonCsv(CsvParseResult result, File pokemonCsv) throws IOException
    {                      
        InputStream input = new FileInputStream(pokemonCsv);        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));        
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                                                                .setHeader(POKEMONHEADERS)
                                                                .setSkipHeaderRecord(true)
                                                                .build())) {
            Set<String> names = new HashSet<>();
            for (CSVRecord record : csvParser) {                
                String name = record.get(POKEMONHEADERS[0]);
                PokemonTypes type = PokemonTypes.valueOf(record.get(POKEMONHEADERS[1]));
                int hitpoints = Integer.parseInt(record.get(POKEMONHEADERS[2]));
                PokemonTypes weakness = PokemonTypes.valueOf(record.get(POKEMONHEADERS[3]));
                int retreatCost = Integer.parseInt(record.get(POKEMONHEADERS[4]));
                EvolutionStage stage = EvolutionStage.valueOf(record.get(POKEMONHEADERS[5]));
                String imageFile = record.get(POKEMONHEADERS[6]);
                if(names.add(name))
                {
                    Pokemon pokemon = new Pokemon(name, type, hitpoints, weakness, retreatCost, stage);
                    pokemon.setImagefile(imageFile);
                    result.addPokemon(pokemon);                    
                }
                else
                {
                    String store = name + "," + type + "," + hitpoints + "," + weakness + "," + retreatCost + "," + stage + "," + imageFile;
                    result.addErrorLog(CsvErrorCodes.DUPLICATE_POKEMON_IN_FILE, store);
                }
            }
        }        
    }
}
