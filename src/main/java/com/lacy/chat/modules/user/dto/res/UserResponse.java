package com.lacy.chat.modules.user.dto.res;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserResponse {


    private String email;

    private String name;
    private String picture;
    private String provider; // GOOGLE, GITHUB
    private String providerId;
}
