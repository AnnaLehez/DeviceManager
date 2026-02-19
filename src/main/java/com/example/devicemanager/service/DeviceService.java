package com.example.devicemanager.service;

import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceCreateDTO;
import com.example.devicemanager.dto.DeviceResponseDTO;
import com.example.devicemanager.dto.DeviceUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    DeviceResponseDTO createDevice(DeviceCreateDTO deviceCreateDTO);
    List<DeviceResponseDTO> getAllDevices();
    DeviceResponseDTO getDeviceById(UUID id);
    List<DeviceResponseDTO> getDevicesByBrand(String brand);
    List<DeviceResponseDTO> getDevicesByState(DeviceState state);
    DeviceResponseDTO updateDevice(UUID id, DeviceUpdateDTO deviceUpdateDTO);
    void deleteDevice(UUID id);
}
