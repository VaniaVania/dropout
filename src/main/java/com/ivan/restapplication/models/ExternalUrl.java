package com.ivan.restapplication.models;

import javax.persistence.*;

@Entity
@Table(name = "users_external_urls")
public class ExternalUrl {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "spotify")
    private String spotify;

    public ExternalUrl() {
    }

    public ExternalUrl(String spotify) {
        this.spotify = spotify;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }
}
