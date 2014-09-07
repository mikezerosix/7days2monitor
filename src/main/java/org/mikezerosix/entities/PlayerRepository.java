package org.mikezerosix.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Settings, String> {


    @Query("update Player p set u.firstname = ?1 where u.lastname = ?2")
    public void updateByRequestToSpawn(long entityId, long clientId, String name, long observedEntity);

}
