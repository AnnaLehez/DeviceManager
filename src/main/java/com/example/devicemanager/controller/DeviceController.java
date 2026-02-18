package com.example.devicemanager.controller;

import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceCreateDTO;
import com.example.devicemanager.dto.DeviceResponseDTO;
import com.example.devicemanager.dto.DeviceUpdateDTO;
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
    public ResponseEntity<DeviceResponseDTO> createDevice(@Valid @RequestBody DeviceCreateDTO deviceCreateDTO) {
        log.debug("Received request to create device: {}", deviceCreateDTO);
        DeviceResponseDTO createdDevice = deviceService.createDevice(deviceCreateDTO);
        return new ResponseEntity<>(createdDevice, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state) {
        log.debug("Received request to fetch devices. Brand: {}, State: {}", brand, state);

        List<DeviceResponseDTO> devices;

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
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable UUID id) {
        log.debug("Received request to fetch device with ID: {}", id);
        DeviceResponseDTO device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @PathVariable UUID id,
            @Valid @RequestBody DeviceUpdateDTO deviceUpdateDTO) {
        log.debug("Received request to update device with ID: {}", id);
        DeviceResponseDTO updatedDevice = deviceService.updateDevice(id, deviceUpdateDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        log.debug("Received request to delete device with ID: {}", id);
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
