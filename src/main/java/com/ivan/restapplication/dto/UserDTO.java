package com.ivan.restapplication.dto;

import com.ivan.restapplication.models.ExplicitContent;
import com.ivan.restapplication.models.ExternalUrl;
import com.ivan.restapplication.models.Follower;
import com.ivan.restapplication.models.Image;

import javax.persistence.*;
import java.util.List;

public class UserDTO {
    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "display_name")
    private String display_name;

    @Column(name = "email")
    private String email;

    @Column(name = "href")
    private String href;

    @Column(name = "spotify_id")
    private String id;

    @Column
    private String product;

    @Column(name = "type")
    private String type;

    @Column(name = "uri")
    private String uri;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_explicit_contents", joinColumns = @JoinColumn(name = "user_id"))
    private ExplicitContent explicit_content;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_external_urls", joinColumns = @JoinColumn(name = "user_id"))
    private ExternalUrl external_urls;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_followers", joinColumns = @JoinColumn(name = "user_id"))
    private Follower followers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @CollectionTable(name = "users_images", joinColumns = @JoinColumn(name = "user_id"))
    private List<Image> images;
}
