package com.turkoid.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by turkoid on 8/21/2016.
 */

@XmlRootElement
@XmlType(name = "user")
public class User {
    private int id;
    private String name;
    private boolean attending;

    public User() {}

    public User(int id, String name, boolean attending) {
        this.id = id;
        this.name = name;
        this.attending = attending;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttending() {
        return attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }
}
