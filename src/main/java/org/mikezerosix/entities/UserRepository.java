package org.mikezerosix.entities;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

    public User findByNameAndPassword(String name, String password);
}
