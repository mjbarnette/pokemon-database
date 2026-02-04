

package io.github.mjbarnette.pokemon.databse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




public class JdbcPokemonDao implements PokemonDao {

    private final HikariDataSource dataSource;
    private static final Logger log = LoggerFactory.getLogger(JdbcPokemonDao.class);
    
    public JdbcPokemonDao(String url, String user, String password) throws IOException
    {              
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        this.dataSource = new HikariDataSource(config);
    }
    
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    private int getTypeId(Connection conn, String type) throws SQLException
    {
        String SqlGetTypeID = """
                              SELECT t.id AS pokemon_Type
                              FROM pokeTypes t
                              WHERE t.type = ?
                              """;
        int pokeTypeId = 0;
        try(PreparedStatement stmt = conn.prepareStatement(SqlGetTypeID))
        {
            stmt.setString(1, type);
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                {
                    pokeTypeId = rs.getInt("pokemon_Type");
                }
            }
        }
        return pokeTypeId;
    }
    
    private int addPokemon(Connection conn, Pokemon newPokemon) throws SQLException
    {
        String sqlAddPoke = """
                            INSERT INTO pokemon (name, imageFile)
                            values
                            (?, ?)
                            RETURNING id;
                            """;
         //Add to Pokemon  
        int pokeIndex = 0;
        try(PreparedStatement stmt1 = conn.prepareStatement(sqlAddPoke))
        {
          stmt1.setString(1, newPokemon.getName());
          stmt1.setString(2, newPokemon.getImageName());                        
          try(ResultSet rs = stmt1.executeQuery())
          {
              if(rs.next())
              {
                  pokeIndex = rs.getInt("id");
              }
          }
        }
        return pokeIndex;
    }
    
    private void addStats(Connection conn, int pokemonId, Pokemon newPokemon) throws SQLException
    {
        String sqlAddStats = """
                             INSERT INTO pokeStats (pokemon_id, hit_points, type_id, weakness_id, retreat_cost)
                             VALUES
                             (?, ?, ?, ?, ?);
                             """;
        
        int pokeTypeId = getTypeId(conn, newPokemon.getType().toString());
        int pokeWeakId = getTypeId(conn, newPokemon.getWeakness().toString());
        try(PreparedStatement stmt4 = conn.prepareStatement(sqlAddStats))
        {
            stmt4.setInt(1, pokemonId);
            stmt4.setInt(2, newPokemon.getHitPoints());
            stmt4.setInt(3, pokeTypeId);
            stmt4.setInt(4, pokeWeakId);
            stmt4.setInt(5, newPokemon.getRetreatCost());
            stmt4.executeUpdate();
        }
    }
    
    private void addMoves(Connection conn, int pokemonId, Pokemon newPokemon) throws SQLException
    {   
        String sqlAddMove = """
            INSERT INTO moves (pokemon_id, name, damage, description)
            VALUES (?, ?, ?, ?)
            RETURNING id
            """;
    
        String sqlAddEnergy = """
            INSERT INTO move_energy (move_id, energy_type_id, amount)
            VALUES (?, ?, ?)
            """;

        for (PokemonMove move : newPokemon.getMoves()) {
            int moveId;

            // Insert move
            try (PreparedStatement stmtMove = conn.prepareStatement(sqlAddMove)) {
                stmtMove.setInt(1, pokemonId);
                stmtMove.setString(2, move.getName());
                stmtMove.setInt(3, move.getDamage());
                stmtMove.setString(4, move.getDescription());

                try (ResultSet rs = stmtMove.executeQuery()) {
                    rs.next();
                    moveId = rs.getInt("id");
                }
            }

            // Insert energy costs
            try (PreparedStatement stmtEnergy = conn.prepareStatement(sqlAddEnergy)) {
                List<PokemonTypes> list = move.getEnergyTypes();
                for (PokemonTypes type : list) {
                    int typeId = getTypeId(conn, type.toString());
                    stmtEnergy.setInt(1, moveId);
                    stmtEnergy.setInt(2, typeId);
                    stmtEnergy.setInt(3, move.getEnergyCost(type));
                    stmtEnergy.executeUpdate();
                }
            }
        }
    }
    
    private void addEvolution(Connection conn, int pokemonId, Pokemon newPokemon) throws SQLException
    {        
        String sql = """
                      INSERT INTO pokeEvolutions (pokemon_id, current_stage, next_stage, nextpoke_id)
                      VALUES
                      (?, ?, null, null)
                     """;
        try(PreparedStatement stmt = conn.prepareStatement(sql))
        {            
            stmt.setInt(1, pokemonId);
            stmt.setString(2, newPokemon.getStage().toString());
            stmt.executeUpdate();
        }
    }
    
    
    @FunctionalInterface
    interface SqlConsumer<T> {
        void accept(T t) throws SQLException;
    }
    
   private void withTransaction(SqlConsumer<Connection> work) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                work.accept(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                log.error("Failed to save Pokemon", e);
                throw e;
            }
        }
   }    
    
    @Override
    public void save(Pokemon newPokemon) throws SQLException
    { 
        if (newPokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null");
        }
        if (newPokemon.getName() == null || newPokemon.getName().isBlank()) {
                throw new IllegalArgumentException("Pokemon name cannot be empty");
        }
        withTransaction(conn -> {
            int pokemonId = addPokemon(conn, newPokemon);
            addStats(conn, pokemonId, newPokemon);
            addMoves(conn, pokemonId, newPokemon);
            addEvolution(conn, pokemonId, newPokemon);
         });  
    }
    
    private Pokemon getStats(Connection conn, String name) throws SQLException
    {
        String sql = """
                     SELECT p.name AS pokemon_name, p.imagefile AS pokemon_image, e.current_stage, s.hit_points AS pokemon_hp, t1.type AS pokemon_type, t2.type AS pokemon_weak, s.retreat_cost AS pokemon_rc
                     FROM pokemon p
                     JOIN pokeStats s ON s.pokemon_id = p.id
                     JOIN pokeTypes t1 ON s.type_id = t1.id
                     JOIN pokeTypes t2 on s.weakness_id = t2.id
                     JOIN pokeEvolutions e on e.pokemon_id = p.id
                     WHERE p.name = ?
                     """; 
         Pokemon monster = null;
         try(PreparedStatement stmt = conn.prepareStatement(sql))
         {            
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                { 
                    String pname = rs.getString("pokemon_name");
                    PokemonTypes type = PokemonTypes.valueOf(rs.getString("pokemon_type"));
                    String image = rs.getString("pokemon_image");
                    int hp = rs.getInt("pokemon_hp");
                    PokemonTypes weak = PokemonTypes.valueOf(rs.getString("pokemon_weak"));
                    EvolutionStage stage = EvolutionStage.valueOf(rs.getString("current_stage"));
                    int retreatCost = rs.getInt("pokemon_rc");
                    monster = new Pokemon(pname, image, type, hp, stage, weak, retreatCost);       
                }
            }
         }
        return monster;
    }
    
    private void getMoves(Connection conn, Pokemon pokemon) throws SQLException {
       String sql = """
           SELECT m.id, m.name, m.damage, m.description,
                  me.energy_type_id, me.amount, t.type
           FROM moves m
           LEFT JOIN move_energy me ON me.move_id = m.id
           LEFT JOIN pokeTypes t ON me.energy_type_id = t.id
           WHERE m.pokemon_id = (SELECT id FROM pokemon WHERE name = ?)
           ORDER BY m.id
           """;

       try (PreparedStatement stmt = conn.prepareStatement(sql)) {
           stmt.setString(1, pokemon.getName());

           try (ResultSet rs = stmt.executeQuery()) {
               Map<Integer, PokemonMove> movesMap = new HashMap<>();

               while (rs.next()) {
                   int moveId = rs.getInt("id");

                   // Get or create move
                   PokemonMove move = movesMap.get(moveId);
                   if (move == null) {
                       move = new PokemonMove(
                           rs.getString("name"),
                           rs.getInt("damage"),
                           rs.getString("description")
                       );
                       movesMap.put(moveId, move);
                   }

                   // Add energy cost (if exists)
                   String energyType = rs.getString("type");
                   if (energyType != null) {
                       int amount = rs.getInt("amount");
                       move.addEnergyCost(PokemonTypes.valueOf(energyType), amount);
                   }
               }

               // Add all moves to pokemon
               movesMap.values().forEach(pokemon::addMoves);
           }
       }
   }
    
    @Override
    public Pokemon findByName(String name) throws SQLException {
        Pokemon monster = null;
        try(Connection conn = getConnection())
        {
            monster = getStats(conn, name);
            if(monster != null)
             getMoves(conn, monster);
        }
        return monster;
    }

    @Override
    public List<Pokemon> getAllPokemon(SortOrder orderBy) throws SQLException {
       
        String sql = """
                    SELECT p.id, p.name AS pokemon_name, p.imagefile AS pokemon_image, 
                            		e.current_stage, s.hit_points AS pokemon_hp, 
                            		t1.type AS pokemon_type, t2.type AS pokemon_weak, 
                            		s.retreat_cost AS pokemon_rc,
                            		m1.name AS move_name, m1.damage, 
                            		m1.description, m3.type AS energy_type,
                            		m2.amount
                            FROM pokemon p
                            JOIN pokeStats s ON s.pokemon_id = p.id
                            JOIN pokeTypes t1 ON s.type_id = t1.id
                            JOIN pokeTypes t2 ON s.weakness_id = t2.id
                            JOIN pokeEvolutions e ON e.pokemon_id = p.id
                            LEFT JOIN moves m1 ON p.id = m1.pokemon_id
                            LEFT JOIN move_energy m2 ON m1.id = m2.move_id
                            LEFT JOIN pokeTypes m3 ON m2.energy_type_id = m3.id
                            ORDER BY """ + " " + orderBy.toString();
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
        
            return buildPokemonList(rs);
        }
    }
    
    private List<Pokemon> buildPokemonList(ResultSet rs) throws SQLException {
        
        Map<Integer, Pokemon> pokemonMap = new LinkedHashMap<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            Pokemon pokemon = pokemonMap.get(id);

            if (pokemon == null) {                
                pokemon = new Pokemon(
                    rs.getString("pokemon_name"),
                    rs.getString("pokemon_image"),
                    PokemonTypes.valueOf(rs.getString("pokemon_type")),
                    rs.getInt("pokemon_hp"),
                    EvolutionStage.valueOf(rs.getString("current_stage")),
                    PokemonTypes.valueOf(rs.getString("pokemon_weak")),
                    rs.getInt("pokemon_rc")
                );
                pokemonMap.put(id, pokemon);
            }
          
            String moveName = rs.getString("move_name");           
            PokemonMove move = null;
            for(PokemonMove m : pokemon.getMoves())
            {
                if(m.getName().equals(moveName))
                {                    
                    move = m;
                }
            }                
            if (moveName != null && move == null) {
                move = new PokemonMove(
                    moveName,                                       
                    rs.getInt("damage"),
                    rs.getString("description")
                );
                move.addEnergyCost(PokemonTypes.valueOf(rs.getString("energy_type")), rs.getInt("amount"));
                pokemon.addMoves(move);                
            }
            else if(move != null)
            {
                System.out.println(move);
                move.addEnergyCost(PokemonTypes.valueOf(rs.getString("energy_type")), rs.getInt("amount"));
            }
        }
        return new ArrayList<>(pokemonMap.values());
    } 

    @Override
    public List<Pokemon> getAllPokemonOfType(PokemonTypes type) throws SQLException {       
        String sql = """
                    SELECT p.id, p.name AS pokemon_name, p.imagefile AS pokemon_image, 
                    		e.current_stage, s.hit_points AS pokemon_hp, 
                    		t1.type AS pokemon_type, t2.type AS pokemon_weak, 
                    		s.retreat_cost AS pokemon_rc,
                    		m1.name AS move_name, m1.damage, 
                    		m1.description, m3.type AS energy_type,
                    		m2.amount
                    FROM pokemon p
                    JOIN pokeStats s ON s.pokemon_id = p.id
                    JOIN pokeTypes t1 ON s.type_id = t1.id
                    JOIN pokeTypes t2 ON s.weakness_id = t2.id
                    JOIN pokeEvolutions e ON e.pokemon_id = p.id
                    LEFT JOIN moves m1 ON p.id = m1.pokemon_id
                    LEFT JOIN move_energy m2 ON m1.id = m2.move_id
                    LEFT JOIN pokeTypes m3 ON m2.energy_type_id = m3.id
                    WHERE t1.type = ? """; 
       
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setString(1, type.toString());
                try(ResultSet rs = stmt.executeQuery()) 
                {        
                    return buildPokemonList(rs);
                }
            }        
    }
    
    private String getName(Connection conn, int pokeID) throws SQLException
    {
        String sql = """
                      SELECT p.name
                      FROM pokemon p
                      WHERE p.id = ?
                     """;
        String name = "";
        try(PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, pokeID);
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                { 
                    name = rs.getString("name");
                }
            }
        }
        return name;
    }
    
    @Override
    public List<Pokemon> getEvolution(String name) throws SQLException {
        List<Pokemon> list = new ArrayList<Pokemon>();
        String sql = """
                     SELECT e.pokemon_id , e.nextpoke_id
                     FROM pokeEvolutions e
                     JOIN pokemon p ON p.id = e.pokemon_id
                     WHERE p.name = ?
                     """;
        try(Connection conn = getConnection())
        {
            try(PreparedStatement stmt = conn.prepareStatement(sql))
            {  
                while(name != null)
                {
                    Pokemon temp = null;
                    stmt.setString(1, name);
                    try(ResultSet rs = stmt.executeQuery())
                    {
                        if(rs.next())
                        { 
                           int nextPoke = rs.getInt("nextpoke_id");                           
                           temp = getStats(conn, name);
                           getMoves(conn, temp);
                           list.add(temp);
                           if(nextPoke != 0)
                           {
                                name = getName(conn, nextPoke);
                           }
                           else
                           {
                               name = null;
                           }                           
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void deletePokemon(String name) throws SQLException {
        withTransaction(conn -> {
            int pokeID = getPokeId(conn, name);
            
            executeDelete(conn, "DELETE FROM pokeEvolutions WHERE pokemon_id = ?", pokeID);
            executeDelete(conn, "DELETE FROM moves WHERE pokemon_id = ?", pokeID);
            executeDelete(conn, "DELETE FROM pokeStats WHERE pokemon_id = ?", pokeID);
            executeDelete(conn, "DELETE FROM pokemon WHERE id = ?", pokeID);
        });
    }
    
    private void executeDelete(Connection conn, String sql, int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    private int getPokeId(Connection conn, String name) throws SQLException
    {
        String sql = """
                     SELECT p.id
                     FROM pokemon p
                     WHERE p.name = ?
                     """;
        int id = 0;
        try(PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                { 
                    id = rs.getInt("id");
                }
            }
        }
        return id;
    }
        
    @Override
    public void setEvolution(Pokemon current, Pokemon nextStage)throws SQLException {
       String sql = """
                    UPDATE pokeEvolutions
                    SET next_stage = ?,
                        nextpoke_id = ?
                    WHERE pokemon_id = ?;
                    """;
       try(Connection conn = getConnection())
        {
            int curPoke = getPokeId(conn, current.getName());
            int nextPoke = getPokeId(conn, nextStage.getName());
            try(PreparedStatement stmt = conn.prepareStatement(sql))
            {            
               stmt.setString(1, nextStage.getStage().toString());
               stmt.setInt(2, nextPoke);
               stmt.setInt(3, curPoke);
               stmt.executeUpdate();
            }
        }
    }

}
