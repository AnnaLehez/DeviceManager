package com.example.devicemanager.controller;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceRequestDTO;
import com.example.devicemanager.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<Device> createDevice(@Valid @RequestBody DeviceRequestDTO deviceRequestDTO) {
        log.debug("Received request to create device: {}", deviceRequestDTO);
        Device createdDevice = deviceService.createDevice(deviceRequestDTO);
        return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state) {
        log.debug("Received request to fetch devices. Brand: {}, State: {}", brand, state);

        List<Device> devices;

        if (brand != null) {
            devices = deviceService.getDevicesByBrand(brand);
        } else if (state != null) {
            devices = deviceService.getDevicesByState(state);
        } else {
            devices = deviceService.getAllDevices();
        }

        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable UUID id) {
        log.debug("Received request to fetch device with ID: {}", id);
        Device device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Device> updateDevice(
            @PathVariable UUID id,
            @RequestBody DeviceRequestDTO deviceRequestDTO) {
        // Note: @Valid is omitted here to allow partial updates (fields can be null)
        log.debug("Received request to update device with ID: {}", id);
        Device updatedDevice = deviceService.updateDevice(id, deviceRequestDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        log.debug("Received request to delete device with ID: {}", id);
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
