package com.study.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
    private Long expiresIn;
    private Long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private int notBeforePolicy;
    private String sessionState;
    private String scope;
}
