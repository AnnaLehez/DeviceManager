package com.example.devicemanager.dto;

import com.example.devicemanager.domain.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for creating a new device")
public record DeviceCreateDTO(
        @Schema(description = "Name of the device", example = "Smart Thermostat", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name is mandatory")
        @Size(min = 1, max = 100, message = "Device name must be between 1 and 100 characters")
        String name,

        @Schema(description = "Brand of the device", example = "Nest", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Brand is mandatory")
        @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
        String brand,

        @Schema(description = "Initial state of the device", example = "AVAILABLE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "State is required")
        DeviceState state
) {

}
