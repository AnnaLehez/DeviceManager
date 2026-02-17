package com.example.devicemanager.dto;

import com.example.devicemanager.domain.DeviceState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequestDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Brand is mandatory")
    private String brand;

    private DeviceState state;
}
