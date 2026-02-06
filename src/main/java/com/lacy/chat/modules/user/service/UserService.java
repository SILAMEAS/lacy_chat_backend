package com.lacy.chat.modules.user.service;

import com.lacy.chat.modules.user.dto.res.UserResponse;

public interface UserService {
    UserResponse findUserById(Long id);
    UserResponse findUserByEmail(String email);
}
