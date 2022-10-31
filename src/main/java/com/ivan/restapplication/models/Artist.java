package com.ivan.restapplication.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Artist {
    @Id
    @Column(name = "id", nullable = false)
    private String id;







    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
