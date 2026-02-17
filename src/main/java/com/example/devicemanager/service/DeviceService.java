package com.example.devicemanager.service;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceRequestDTO;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    Device createDevice(DeviceRequestDTO deviceRequestDTO);
    List<Device> getAllDevices();
    Device getDeviceById(UUID id);
    List<Device> getDevicesByBrand(String brand);
    List<Device> getDevicesByState(DeviceState state);
    Device updateDevice(UUID id, DeviceRequestDTO deviceRequestDTO);
    void deleteDevice(UUID id);
}
