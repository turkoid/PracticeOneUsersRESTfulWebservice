package com.turkoid.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by turkoid on 8/21/2016.
 */

@XmlRootElement
@XmlType(name = "user")
public class User {
    private Integer id;
    private String name;
    private Boolean attending;

    public User() {}

    public User(Integer id, String name, Boolean attending) {
        this.id = id;
        this.name = name;
        this.attending = attending;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAttending() {
        return attending;
    }

    public void setAttending(Boolean attending) {
        this.attending = attending;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attending=" + attending +
                '}';
    }
}
