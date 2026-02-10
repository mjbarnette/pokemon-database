
package io.github.mjbarnette.pokemon.database.utility;

import io.github.mjbarnette.pokemon.database.entity.Pokemon;
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
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class CsvUtility {
    
    private static final String[] POKEMONHEADERS = {"Name", "Type", "Hit Points", "Weakness", "Retreat Cost", "Stage", "Image File"};
    private static final String[] MOVEHEADERS = {"Pokemon Name", "Title", "damage", "Description", "Energy Types", "Costs"};
    private static final String[] EVOLUTIONHEADERS = {"Current Pokemon", "Next Pokemon"};
    
    public static List<Pokemon> parseCsvFiles(File pokemon, File moves, File evolutions) throws IOException
    {
        List<Pokemon> list = parsePokemonCsv(pokemon);
        HashMap<String, Pokemon> graph = new HashMap<>();
        for(Pokemon p : list)
        {
            graph.put(p.getName(), p);
        }
        parseMoveCsv(graph, moves); 
        parseEvolutions(graph, evolutions);
        return list;
    }
    
    private static void parseEvolutions(HashMap<String, Pokemon> graph, File evolutions) throws IOException 
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
                    pokemonCurrent.addEvolutions(pokemonNext);
                }
            }
        }
    }
    
    private static void parseMoveCsv(HashMap<String, Pokemon> graph, File moves) throws IOException
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
                  HashMap<PokemonTypes, Integer> cost = new HashMap<>();
                  for(int i = 0; i < types.length; i++)
                  {
                      cost.put(PokemonTypes.valueOf(types[i]), Integer.parseInt(amount[i]));
                  }
                  //log.info("{} : {} ", name, title);
                  Pokemon pokemon = graph.get(name);
                  log.info("{} : {}", pokemon, name);
                  pokemon.addMoves(title, damage, description, cost);
            }
        }         
    }
    
    private static List<Pokemon> parsePokemonCsv(File pokemonCsv) throws IOException
    {                      
        InputStream input = new FileInputStream(pokemonCsv);
        List<Pokemon> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input));        
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                                                                .setHeader(POKEMONHEADERS)
                                                                .setSkipHeaderRecord(true)
                                                                .build())) {
            for (CSVRecord record : csvParser) {                
                String name = record.get(POKEMONHEADERS[0]);
                PokemonTypes type = PokemonTypes.valueOf(record.get(POKEMONHEADERS[1]));
                int hitpoints = Integer.parseInt(record.get(POKEMONHEADERS[2]));
                PokemonTypes weakness = PokemonTypes.valueOf(record.get(POKEMONHEADERS[3]));
                int retreatCost = Integer.parseInt(record.get(POKEMONHEADERS[4]));
                EvolutionStage stage = EvolutionStage.valueOf(record.get(POKEMONHEADERS[5]));
                String imageFile = record.get(POKEMONHEADERS[6]);
                Pokemon pokemon = new Pokemon(name, type, hitpoints, weakness, retreatCost, stage);
                pokemon.setImagefile(imageFile);
                list.add(pokemon);
            }
        }
        return list;
    }
}
