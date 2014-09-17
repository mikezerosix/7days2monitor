package org.mikezerosix.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface StatRepository extends CrudRepository<Stat, Long> {


    @Query("SELECT s FROM Stat s where s.id = (SELECT max(s.id) FROM Stat s) order by s.id desc")
    public Stat getLastStat();

    @Query("SELECT max(s.players), max(s.zombies), max(s.entities), max(s.entities2), max(s.items), max(s.memHeap), max(s.memMax), max(s.chunks), max(s.cgo), max(s.fps) FROM Stat s")
    public Object[] getMaxStat();

    @Override
    @Query("SELECT s FROM Stat s order by s.id desc")
    Iterable<Stat> findAll();
}
