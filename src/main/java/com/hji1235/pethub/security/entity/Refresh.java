package com.hji1235.pethub.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Refresh {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String token;

    private String expiration;

    public Refresh(String username, String token, String expiration) {
        this.username = username;
        this.token = token;
        this.expiration = expiration;
    }
}
