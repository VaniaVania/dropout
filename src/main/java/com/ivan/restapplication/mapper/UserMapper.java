package com.ivan.restapplication.mapper;

import com.ivan.restapplication.model.entity.User;
import com.ivan.restapplication.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper()
public interface UserMapper {
    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);
}
