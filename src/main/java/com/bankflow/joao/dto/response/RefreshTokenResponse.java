package com.bankflow.joao.dto.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
