package com.lacy.chat.modules.user.service;

import com.lacy.chat.config.core.crud.AbstractCrudCommon;
import com.lacy.chat.modules.user.dto.res.UserResponse;
import com.lacy.chat.modules.user.model.User;
import com.lacy.chat.modules.user.model.UserMapper;
import com.lacy.chat.modules.user.repository.UserRepository;
import com.lacy.chat.share.exception.NotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends AbstractCrudCommon<UserMapper, User, Long, UserRepository> implements UserService {

    protected UserServiceImpl(UserRepository baseRepository, UserMapper mapper) {
        super(baseRepository, mapper);
    }

    @Override
    public UserResponse findUserById(Long id) {
        return super.mapper.toUserResponse(super.findById(id));
    }

    @Override
    public UserResponse findUserByEmail(String email) {
        return super.mapper.toUserResponse(super.baseRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found")));
    }


}
