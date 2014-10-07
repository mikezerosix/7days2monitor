package org.mikezerosix.entities;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {


    public Player findByName(String name);
    public Player findByEntityId(long entityId);
    public Player findBySteamId(String steamId);
}
