ALTER TABLE pokestats_evolutions
DROP CONSTRAINT fknlfolae1v1lw7rwntvh3yags7;

-- 1. Add new surrogate primary key
ALTER TABLE pokeevolutions
ADD COLUMN id SERIAL;

-- 2. Drop old primary key on pokemon_id
ALTER TABLE pokeevolutions
DROP CONSTRAINT pokeevolutions_pkey;

-- 3. Promote id to primary key
ALTER TABLE pokeEvolutions
ADD CONSTRAINT pokeevolutions_pkey PRIMARY KEY (id);

ALTER TABLE pokestats_evolutions
ADD COLUMN evolution_id int;

ALTER TABLE pokestats_evolutions
ADD CONSTRAINT fknlfolae1v1lw7rwntvh3yags7
FOREIGN KEY (evolution_id)
REFERENCES pokeevolutions(id);