package com.hji1235.pethub.security.controller;

import com.hji1235.pethub.security.dto.ReissueTokenDto;
import com.hji1235.pethub.security.service.RefreshService;
import com.hji1235.pethub.security.util.CookieUtil;
import com.hji1235.pethub.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = CookieUtil.getCookieValue(request, "Refresh");

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }
        ReissueTokenDto reissueTokenDto = refreshService.reissue(refresh);

        response.setHeader("Authorization", "Bearer " + reissueTokenDto.getNewAccess());
        response.addCookie(CookieUtil.createRefreshCookie("Refresh", reissueTokenDto.getNewRefresh(), jwtUtil.getRefreshTokenExpiredMs()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
