package com.lacy.chat.modules.user.model;

import com.lacy.chat.modules.user.dto.res.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUser(UserResponse userResponse);
}
