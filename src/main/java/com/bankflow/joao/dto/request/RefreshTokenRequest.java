package com.bankflow.joao.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank String accessToken
) {
}
