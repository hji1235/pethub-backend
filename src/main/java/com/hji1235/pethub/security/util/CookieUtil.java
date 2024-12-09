package com.hji1235.pethub.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JWTUtil jwtUtil;

    public static Cookie createRefreshCookie(String key, String value, long expiredMs) {
        int maxAge = (int) (expiredMs / 1000);
        Cookie refreshCookie = new Cookie(key, value);
        refreshCookie.setMaxAge(maxAge);
//        refreshCookie.setSecure(true); // https 사용 시
//        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        return refreshCookie;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        String cookieValue = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                cookieValue = cookie.getValue();
            }
        }
        return cookieValue;
    }
}
