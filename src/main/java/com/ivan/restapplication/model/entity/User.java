package com.ivan.restapplication.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long dbId;

    @Column(name = "country")
    private String country;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "email")
    private String email;

    @Column(name = "href")
    private String href;

    @Column(name = "spotify_id")
    private String id;

    @Column(name = "product")
    private String product;

    @Column(name = "type")
    private String type;

    @Column(name = "uri")
    private String uri;

    @Column(name = "country_image")
    private String countryImage;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_explicit_contents", joinColumns = @JoinColumn(name = "user_id"))
    private ExplicitContent explicitContent;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_external_urls", joinColumns = @JoinColumn(name = "user_id"))
    private ExternalUrl externalUrls;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_followers", joinColumns = @JoinColumn(name = "user_id"))
    private Follower followers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_images", joinColumns = @JoinColumn(name = "user_id"))
    private List<Image> images;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
