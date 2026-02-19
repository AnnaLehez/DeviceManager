package com.example.devicemanager.dto;


import com.example.devicemanager.domain.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for updating an existing device")
public record DeviceUpdateDTO(
        @Schema(description = "New name of the device", example = "Updated Thermostat")
        @Size(min = 1, max = 100, message = "Device name must be between 1 and 100 characters")
        String name,

        @Schema(description = "New brand of the device", example = "Updated Brand")
        @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
        String brand,

        @Schema(description = "New state of the device", example = "IN_USE")
        DeviceState state
) {}
