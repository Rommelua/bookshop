package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.user.UserResponseDto;
import com.bookshop.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);
}
