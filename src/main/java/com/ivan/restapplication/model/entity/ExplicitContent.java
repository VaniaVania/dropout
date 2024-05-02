package com.ivan.restapplication.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_explicit_contents")
@NoArgsConstructor
@Getter
@Setter
public class ExplicitContent {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean filter_enabled, filter_locked;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public ExplicitContent(boolean filter_enabled, boolean filter_locked) {
        this.filter_enabled = filter_enabled;
        this.filter_locked = filter_locked;
    }
}
