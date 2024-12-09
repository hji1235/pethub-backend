package com.hji1235.pethub.security.service;

import com.hji1235.pethub.security.dto.ReissueTokenDto;
import com.hji1235.pethub.security.entity.Refresh;
import com.hji1235.pethub.security.repository.RefreshRepository;
import com.hji1235.pethub.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshService {

    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public void saveRefresh(String email, String token) {
        Refresh refresh = getRefresh(email, token);
        refreshRepository.save(refresh);
    }

    @Transactional
    public ReissueTokenDto reissue(String refresh) {
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다."); // 추후 다른 Exception 사용 및 exception 핸들링 필요
        }

        if (!isRefreshTokenValid(refresh)) {
            throw new IllegalArgumentException("리프레시 토큰이 검증 실패하였습니다."); // 추후 다른 Exception 사용 및 exception 핸들링 필요
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createAccessToken(email, role);
        String newRefresh = jwtUtil.createRefreshToken(email);

        refreshRepository.deleteByToken(refresh);
        Refresh refreshEntity = getRefresh(email, newRefresh);
        refreshRepository.save(refreshEntity);

        return new ReissueTokenDto(newAccess, newRefresh);
    }

    public boolean isRefreshTokenValid(String token) {
        return refreshRepository.existsByToken(token);
    }

    @Transactional
    public void deleteRefresh(String token) {
        refreshRepository.deleteByToken(token);
    }

    private Refresh getRefresh(String email, String token) {
        Date date = new Date(System.currentTimeMillis() + jwtUtil.getRefreshTokenExpiredMs());
        return new Refresh(email, token, date);
    }
}
