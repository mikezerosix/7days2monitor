package org.mikezerosix.service;

import org.eclipse.jetty.util.security.Credential;
import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    public static final String ADMIN = "admin";
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void init(String adminPassword) {
        if (userRepository.count() < 1) {
            String password = getPasswordHash(adminPassword);
            final String msg = "New installation, creating user: admin , password: " + password;
            log.info(msg);
            System.out.println(msg);
            final User user = new User();
            user.setName(ADMIN);
            user.setPassword(adminPassword);
            userRepository.save(user);
        }
    }

    private String getPasswordHash(String password) {
        return Credential.MD5.digest(password);
    }


}
