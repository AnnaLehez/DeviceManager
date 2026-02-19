package com.example.devicemanager.dto;

import com.example.devicemanager.domain.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO representing a device in the system")
public record DeviceResponseDTO(
        @Schema(description = "Unique identifier of the device", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Name of the device", example = "Smart Thermostat")
        String name,

        @Schema(description = "Brand of the device", example = "Nest")
        String brand,

        @Schema(description = "Current state of the device", example = "AVAILABLE")
        DeviceState state,

        @Schema(description = "Timestamp when the device was created", example = "2023-10-27T10:00:00")
        LocalDateTime creationTime
) {}
