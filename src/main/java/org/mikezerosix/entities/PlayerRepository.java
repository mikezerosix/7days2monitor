package org.mikezerosix.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface PlayerRepository extends CrudRepository<Player, Long> {


    public Player findByName(String name);
    public Player findByEntityId(long entityId);
    public Player findBySteamId(String steamId);
    public List<Player> findByOnlineAndLastSyncLessThan(boolean online, Date lastSync);
}
