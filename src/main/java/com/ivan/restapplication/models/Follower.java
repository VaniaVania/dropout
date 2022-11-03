package com.ivan.restapplication.models;

import javax.persistence.*;

@Entity
@Table(name = "users_followers")
public class Follower {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String href;

    private Integer total;

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
}
