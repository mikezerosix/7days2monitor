package org.mikezerosix.entities;

import org.springframework.data.repository.CrudRepository;


public interface ConnectionRepository extends CrudRepository<ConnectionSettings, Long> {
    public ConnectionSettings findByType(ConnectionType type);
}
