package com.example.devicemanager.dto;


import com.example.devicemanager.domain.DeviceState;
import jakarta.validation.constraints.Size;

public record DeviceUpdateDTO(
        @Size(min = 1, max = 100, message = "Device name must be between 1 and 100 characters")
        String name,

        @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
        String brand,

        DeviceState state
) {}
