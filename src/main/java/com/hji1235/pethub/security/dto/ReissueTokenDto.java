package com.hji1235.pethub.security.dto;

import lombok.Getter;

@Getter
public class ReissueTokenDto {

    private String newAccess;

    private String newRefresh;

    public ReissueTokenDto(String newAccess, String newRefresh) {
        this.newAccess = newAccess;
        this.newRefresh = newRefresh;
    }
}
