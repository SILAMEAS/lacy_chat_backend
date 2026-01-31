package com.lacy.chat.modules.user.service;

import com.lacy.chat.modules.user.model.User;
import com.lacy.chat.modules.user.repository.UserRepository;
import com.lacy.chat.share.enums.EnumProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser =
                new DefaultOAuth2UserService().loadUser(request);

        String email = oauthUser.getAttribute("email");

        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(oauthUser.getAttribute("name"));
                    u.setPicture(oauthUser.getAttribute("picture"));
                    u.setProvider(EnumProvider.GOOGLE.toString());
                    u.setProviderId(oauthUser.getAttribute("sub"));
                    return userRepository.save(u);
                });

        return oauthUser;
    }
}
