/*
*
*V2__add_varchar_forenums.sql
*/

ALTER TABLE move_energy ADD COLUMN energy_type VARCHAR(20);

UPDATE move_energy
SET energy_type = (
    SELECT t.type
    FROM pokeTypes t
    WHERE t.id = move_energy.energy_type_id
);


ALTER TABLE pokestats ADD COLUMN type VARCHAR(20);

UPDATE pokestats p
SET type = (
    SELECT t.type
    FROM pokeTypes t
    WHERE p.type_id = t.id
);

ALTER TABLE pokestats ADD COLUMN weakness VARCHAR(20);

UPDATE pokestats p
SET weakness = (
    SELECT t.type
    FROM pokeTypes t
    WHERE  p.weakness_id = t.id
); 