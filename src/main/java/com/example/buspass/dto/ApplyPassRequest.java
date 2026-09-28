package com.example.buspass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApplyPassRequest(
        @NotNull Long studentId,
        @NotNull Long routeId,
        @NotBlank String boardingPoint,
        @NotBlank String photoReference
) {}
