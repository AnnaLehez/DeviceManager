package com.example.devicemanager.service;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceRequestDTO;
import com.example.devicemanager.exception.InvalidDeviceStateException;
import com.example.devicemanager.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public Device createDevice(DeviceRequestDTO deviceRequestDTO) {
        log.debug("Creating new device with name: {} and brand: {}", deviceRequestDTO.getName(), deviceRequestDTO.getBrand());
        Device device = new Device(deviceRequestDTO.getName(), deviceRequestDTO.getBrand(), deviceRequestDTO.getState());
        if (deviceRequestDTO.getState() != null) {
            device.setState(deviceRequestDTO.getState());
        }
        Device savedDevice = deviceRepository.save(device);
        log.info("Device created successfully with ID: {}", savedDevice.getId());

        return savedDevice;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> getAllDevices() {
        log.debug("Fetching all devices");
        return deviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Device getDeviceById(UUID id) {
        log.debug("Fetching device with ID: {}", id);
        return deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Device not found with ID: {}", id);
                    return new EntityNotFoundException("Device not found with id: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> getDevicesByBrand(String brand) {
        log.debug("Fetching devices with brand: {}", brand);
        return deviceRepository.findByBrand(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Device> getDevicesByState(DeviceState state) {
        log.debug("Fetching devices with state: {}", state);
        return deviceRepository.findByState(state);
    }

    @Override
    @Transactional
    public Device updateDevice(UUID id, DeviceRequestDTO deviceRequestDTO) {
        log.debug("Updating device with ID: {} with payload: {}", id, deviceRequestDTO);
        Device existingDevice = getDeviceById(id);

        if (existingDevice.getState() == DeviceState.IN_USE) {
            if (deviceRequestDTO.getName() != null && !existingDevice.getName().equals(deviceRequestDTO.getName())) {
                log.warn("Attempt to update name of IN_USE device with ID: {}", id);
                throw new InvalidDeviceStateException("Cannot update name when device is IN_USE");
            }
            if (deviceRequestDTO.getBrand() != null && !existingDevice.getBrand().equals(deviceRequestDTO.getBrand())) {
                log.warn("Attempt to update brand of IN_USE device with ID: {}", id);
                throw new InvalidDeviceStateException("Cannot update brand when device is IN_USE");
            }
        }

        if (deviceRequestDTO.getName() != null) {
            existingDevice.setName(deviceRequestDTO.getName());
        }
        if (deviceRequestDTO.getBrand() != null) {
            existingDevice.setBrand(deviceRequestDTO.getBrand());
        }
        if (deviceRequestDTO.getState() != null) {
            existingDevice.setState(deviceRequestDTO.getState());
        }

        Device updatedDevice = deviceRepository.save(existingDevice);
        log.info("Device with ID: {} updated successfully", id);
        return updatedDevice;
    }

    @Override
    @Transactional
    public void deleteDevice(UUID id) {
        log.debug("Deleting device with ID: {}", id);
        Device device = getDeviceById(id);
        if (device.getState() == DeviceState.IN_USE) {
            log.warn("Attempt to delete IN_USE device with ID: {}", id);
            throw new InvalidDeviceStateException("Cannot delete device when it is IN_USE");
        }
        deviceRepository.delete(device);
        log.info("Device with ID: {} deleted successfully", id);
    }
}
