package com.hji1235.pethub.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    private String email;

    private String password;
}