package com.ivan.restapplication.dto;

import com.ivan.restapplication.models.ExplicitContent;
import com.ivan.restapplication.models.ExternalUrl;
import com.ivan.restapplication.models.Follower;
import com.ivan.restapplication.models.Image;

import javax.persistence.*;
import java.util.List;


public class UserDTO {

    private String name;

    private String country;

    private String display_name;

    private String email;

    private String href;

    private String id;

    private String product;

    private String type;

    private String uri;

    private ExplicitContent explicit_content;

    private ExternalUrl external_urls;

    private Follower followers;

    private List<Image> images;

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

    public void setId(String id) {
        this.id = id;
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

    public void setExplicit_content(ExplicitContent explicit_content) {
        this.explicit_content = explicit_content;
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
