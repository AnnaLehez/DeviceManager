package com.example.devicemanager.dto;

import com.example.devicemanager.domain.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeviceRequestDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message = "Device name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Brand is mandatory")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    private String brand;

    @NotNull(message = "State is required")
    private DeviceState state;
}
