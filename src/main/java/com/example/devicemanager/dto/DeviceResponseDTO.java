package com.example.devicemanager.dto;

import com.example.devicemanager.domain.DeviceState;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceResponseDTO(
        UUID id,
        String name,
        String brand,
        DeviceState state,
        LocalDateTime creationTime
) {}
