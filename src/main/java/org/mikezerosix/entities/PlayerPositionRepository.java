package org.mikezerosix.entities;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PlayerPositionRepository extends CrudRepository<PlayerPosition, Long> {

    public List<PlayerPosition> findByPlayerId(long playerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PlayerPosition s WHERE s.ts < ?")
    public void cleanup(long retain);
}
