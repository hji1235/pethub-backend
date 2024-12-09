package com.hji1235.pethub.user.controller;

import com.hji1235.pethub.user.dto.UserJoinRequest;
import com.hji1235.pethub.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String join(@RequestBody UserJoinRequest request) {
        joinService.join(request);
        return "회원가입 ok";
    }
}
