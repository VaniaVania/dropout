package com.ivan.restapplication.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long db_id;

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

    @Column
    private String product;

    @Column(name = "type")
    private String type;

    @Column(name = "uri")
    private String uri;

    @Column(name = "country_image")
    private String countryImage;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String country, String displayName, String email, String href, String id, String product, String type, String uri, String countryImage, ExplicitContent explicit_content, ExternalUrl external_urls, Follower followers, List<Image> images, LocalDateTime createdAt) {
        this.country = country;
        this.displayName = displayName;
        this.email = email;
        this.href = href;
        this.id = id;
        this.product = product;
        this.type = type;
        this.uri = uri;
        this.countryImage = countryImage;
        this.explicit_content = explicit_content;
        this.external_urls = external_urls;
        this.followers = followers;
        this.images = images;
        this.createdAt = createdAt;
    }

    public Long getDb_id() {
        return db_id;
    }

    public void setDb_id(Long id) {
        this.db_id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCountryImage() {
        return countryImage;
    }

    public void setCountryImage(String countryImage) {
        this.countryImage = countryImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "db_id=" + db_id +
                ", country='" + country + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", href='" + href + '\'' +
                ", id='" + id + '\'' +
                ", product='" + product + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", explicit_content=" + explicit_content +
                ", external_urls=" + external_urls +
                ", followers=" + followers +
                ", images=" + images +
                '}';
    }
}
