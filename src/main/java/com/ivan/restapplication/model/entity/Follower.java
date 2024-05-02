package com.ivan.restapplication.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users_followers")
public class Follower {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "href")
    private String href;

    @Column(name = "total")
    private Integer total;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Follower() {
    }

    public Follower(String href, Integer total) {
        this.href = href;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
