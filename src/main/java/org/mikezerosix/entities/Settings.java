package org.mikezerosix.entities;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Settings {
    @Id
    private String id;
    private String value;

    public Settings() {
    }

    public Settings(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
