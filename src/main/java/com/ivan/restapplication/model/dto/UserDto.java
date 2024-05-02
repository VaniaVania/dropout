package com.ivan.restapplication.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ivan.restapplication.model.entity.ExplicitContent;
import com.ivan.restapplication.model.entity.ExternalUrl;
import com.ivan.restapplication.model.entity.Follower;
import com.ivan.restapplication.model.entity.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private String country;

    @JsonProperty("display_name")
    private String displayName;
    private String email;
    private String href;
    private String id;
    private String product;
    private String type;
    private String uri;
    @JsonProperty("explicit_content")
    private ExplicitContent explicitContent;
    @JsonProperty("external_urls")
    private ExternalUrl externalUrls;
    private Follower followers;
    private List<Image> images;
}
