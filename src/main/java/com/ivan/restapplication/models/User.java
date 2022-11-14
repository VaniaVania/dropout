package com.ivan.restapplication.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long db_id;

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


    public User() {
    }

    public User(String name, String country, String display_name, String email, String href, String id, String product, String type, String uri, ExplicitContent explicit_content, ExternalUrl external_urls, Follower followers, List<Image> images) {
        this.name = name;
        this.country = country;
        this.display_name = display_name;
        this.email = email;
        this.href = href;
        this.id = id;
        this.product = product;
        this.type = type;
        this.uri = uri;
        this.explicit_content = explicit_content;
        this.external_urls = external_urls;
        this.followers = followers;
        this.images = images;
    }

    public Long getDb_id() {
        return db_id;
    }

    public void setDb_id(Long id) {
        this.db_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String spotifyId) {
        this.id = spotifyId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ExplicitContent getExplicit_content() {
        return explicit_content;
    }

    public void setExplicit_content(ExplicitContent explicitContent) {
        this.explicit_content = explicitContent;
    }

    public ExternalUrl getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(ExternalUrl external_urls) {
        this.external_urls = external_urls;
    }

    public Follower getFollowers() {
        return followers;
    }

    public void setFollowers(Follower followers) {
        this.followers = followers;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
