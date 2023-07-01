package com.ivan.restapplication.models;


import javax.persistence.*;

@Entity
@Table(name = "users_explicit_contents")
public class ExplicitContent {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean filter_enabled, filter_locked;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public ExplicitContent() {
    }

    public ExplicitContent(boolean filter_enabled, boolean filter_locked) {
        this.filter_enabled = filter_enabled;
        this.filter_locked = filter_locked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFilter_enabled() {
        return filter_enabled;
    }

    public void setFilter_enabled(boolean filter_enabled) {
        this.filter_enabled = filter_enabled;
    }

    public boolean isFilter_locked() {
        return filter_locked;
    }

    public void setFilter_locked(boolean filter_locked) {
        this.filter_locked = filter_locked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
