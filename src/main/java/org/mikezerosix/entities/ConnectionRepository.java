package org.mikezerosix.entities;

import org.springframework.data.repository.CrudRepository;


public interface ConnectionRepository extends CrudRepository<Linkage, Long> {
    public Linkage findByType(ConnectionType type);
}
