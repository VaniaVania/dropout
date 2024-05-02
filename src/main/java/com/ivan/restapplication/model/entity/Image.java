package com.ivan.restapplication.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users_images")
public class Image {

    @Id
    @Column(name = "images_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url")
    private String url;

    private Integer height, weight;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Image() {
    }

    public Image(String url, Integer height, Integer weight) {
        this.url = url;
        this.height = height;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
