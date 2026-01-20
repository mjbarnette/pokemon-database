/**
 * Author:  mjbar
 * Created: Jan 19, 2026
 */
-- Complete initial schema with correct design
CREATE TABLE pokeTypes (
    id SERIAL PRIMARY KEY,
    type VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO pokeTypes (type) VALUES 
    ('Normal'), ('Fire'), ('Water'), ('Grass'),
    ('Electric'), ('Ice'), ('Fighting'), ('Poison'),
    ('Ground'), ('Flying'), ('Psychic'), ('Bug'),
    ('Rock'), ('Ghost'), ('Dragon'), ('DARK'),
    ('Steel'), ('Fairy');

CREATE TABLE pokemon (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    imageFile VARCHAR(100)
);

CREATE TABLE pokeStats (
    pokemon_id INT PRIMARY KEY REFERENCES pokemon(id) ON DELETE CASCADE,
    hit_points INT NOT NULL,
    type_id INT NOT NULL REFERENCES pokeTypes(id),
    weakness_id INT REFERENCES pokeTypes(id),
    retreat_cost INT DEFAULT 0
);

CREATE TABLE pokeEvolutions (
    pokemon_id INT PRIMARY KEY REFERENCES pokemon(id) ON DELETE CASCADE,
    current_stage VARCHAR(20) NOT NULL,
    next_stage VARCHAR(20),
    nextpoke_id INT REFERENCES pokemon(id)
);

CREATE TABLE moves (
    id SERIAL PRIMARY KEY,
    pokemon_id INT NOT NULL REFERENCES pokemon(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    damage INT DEFAULT 0,
    description TEXT
);

CREATE TABLE move_energy (
    move_id INT NOT NULL REFERENCES moves(id) ON DELETE CASCADE,
    energy_type_id INT NOT NULL REFERENCES pokeTypes(id),
    amount INT NOT NULL CHECK (amount > 0),
    PRIMARY KEY (move_id, energy_type_id)
);

-- Indexes for performance
CREATE INDEX idx_pokemon_name ON pokemon(name);
CREATE INDEX idx_moves_pokemon ON moves(pokemon_id);
CREATE INDEX idx_move_energy_move ON move_energy(move_id);
