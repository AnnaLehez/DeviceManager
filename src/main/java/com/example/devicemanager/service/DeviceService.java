package com.example.devicemanager.service;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceCreateDTO;
import com.example.devicemanager.dto.DeviceUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    Device createDevice(DeviceCreateDTO deviceCreateDTO);
    List<Device> getAllDevices();
    Device getDeviceById(UUID id);
    List<Device> getDevicesByBrand(String brand);
    List<Device> getDevicesByState(DeviceState state);
    Device updateDevice(UUID id, DeviceUpdateDTO deviceUpdateDTO);
    void deleteDevice(UUID id);
}
