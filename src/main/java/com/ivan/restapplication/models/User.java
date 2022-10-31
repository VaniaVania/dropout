package com.ivan.restapplication.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "href")
    private String href;

    @ElementCollection
    @CollectionTable(name = "user_images", joinColumns = @JoinColumn(name = "images"))
    private List<Object> images;

    @Column(name = "product")
    private String product;

    @Column(name = "type")
    private String type;

    @Column(name = "uri")
    private String uri;

    @Column(name = "country")
    private String country;

    @Column(name = "display_name")
    private String display_name;

    @Column(name = "email")
    private String email;

    @ElementCollection
    @CollectionTable(name = "explicit_content", joinColumns = @JoinColumn(name = "user_explicit_content"))
    private List<Object> explicit_content;

    @ElementCollection
    @CollectionTable(name = "external_urls", joinColumns = @JoinColumn(name = "user_external_urls"))
    private List<Object> external_urls;

    @ElementCollection
    @CollectionTable(name = "followers", joinColumns = @JoinColumn(name = "user_followers"))
    private List<Object> followers;


    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setImages(List<Object> images) {
        this.images = images;
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

    public List<Object> getExplicit_content() {
        return explicit_content;
    }

    public void setExplicit_content(List<Object> explicit_content) {
        this.explicit_content = explicit_content;
    }

    public List<Object> getExternal_urls() {
        return external_urls;
    }

    public void setExternal_urls(List<Object> external_urls) {
        this.external_urls = external_urls;
    }

    public List<Object> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Object> followers) {
        this.followers = followers;
    }
}
