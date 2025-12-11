package com.ivan.restapplication.mapper;

import com.ivan.restapplication.model.entity.User;
import com.ivan.restapplication.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper()
public interface UserMapper {
    UserResponse entityToDto(User user);

    User dtoToEntity(UserResponse userResponse);
}
