package com.example.devicemanager.service;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceRequestDTO;
import com.example.devicemanager.exception.InvalidDeviceStateException;
import com.example.devicemanager.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    @Captor
    private ArgumentCaptor<Device> deviceCaptor;

    private Device device;
    private DeviceRequestDTO deviceRequestDTO;
    private UUID deviceId;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        device = new Device("Test Device", "Test Brand", DeviceState.AVAILABLE);
        
        deviceRequestDTO = new DeviceRequestDTO();
        deviceRequestDTO.setName("Test Device");
        deviceRequestDTO.setBrand("Test Brand");
        deviceRequestDTO.setState(DeviceState.AVAILABLE);
    }

    @Test
    @DisplayName("Create device should save and return the device")
    void createDevice_ShouldReturnSavedDevice() {
        // Given
        given(deviceRepository.save(any(Device.class))).willReturn(device);

        // When
        Device createdDevice = deviceService.createDevice(deviceRequestDTO);

        // Then
        then(deviceRepository).should().save(deviceCaptor.capture());
        Device capturedDevice = deviceCaptor.getValue();

        assertThat(capturedDevice.getName()).isEqualTo(deviceRequestDTO.getName());
        assertThat(capturedDevice.getBrand()).isEqualTo(deviceRequestDTO.getBrand());
        assertThat(capturedDevice.getState()).isEqualTo(deviceRequestDTO.getState());

        assertThat(createdDevice).isEqualTo(device);
    }

    @Test
    @DisplayName("Get all devices should return a list of devices")
    void getAllDevices_ShouldReturnListOfDevices() {
        // Given
        given(deviceRepository.findAll()).willReturn(List.of(device));

        // When
        List<Device> devices = deviceService.getAllDevices();

        // Then
        assertThat(devices).isNotEmpty().hasSize(1);
        then(deviceRepository).should().findAll();
    }

    @Test
    @DisplayName("Get device by ID should return device when found")
    void getDeviceById_ShouldReturnDevice_WhenFound() {
        // Given
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        // When
        Device foundDevice = deviceService.getDeviceById(deviceId);

        // Then
        assertThat(foundDevice).isNotNull();
        assertThat(foundDevice).isEqualTo(device);
        then(deviceRepository).should().findById(deviceId);
    }

    @Test
    @DisplayName("Get device by ID should throw EntityNotFoundException when not found")
    void getDeviceById_ShouldThrowException_WhenNotFound() {
        // Given
        given(deviceRepository.findById(deviceId)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> deviceService.getDeviceById(deviceId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Device not found with id: " + deviceId);
        
        then(deviceRepository).should().findById(deviceId);
    }

    @Test
    @DisplayName("Get devices by brand should return list of devices")
    void getDevicesByBrand_ShouldReturnListOfDevices() {
        // Given
        String brand = "Test Brand";
        given(deviceRepository.findByBrand(brand)).willReturn(List.of(device));

        // When
        List<Device> devices = deviceService.getDevicesByBrand(brand);

        // Then
        assertThat(devices).isNotEmpty().hasSize(1);
        assertThat(devices.getFirst().getBrand()).isEqualTo(brand);
        then(deviceRepository).should().findByBrand(brand);
    }

    @Test
    @DisplayName("Get devices by state should return list of devices")
    void getDevicesByState_ShouldReturnListOfDevices() {
        // Given
        DeviceState state = DeviceState.AVAILABLE;
        given(deviceRepository.findByState(state)).willReturn(List.of(device));

        // When
        List<Device> devices = deviceService.getDevicesByState(state);

        // Then
        assertThat(devices).isNotEmpty().hasSize(1);
        assertThat(devices.getFirst().getState()).isEqualTo(state);
        then(deviceRepository).should().findByState(state);
    }

    @Test
    @DisplayName("Update device should update fields when device exists and is AVAILABLE")
    void updateDevice_ShouldUpdateFields_WhenDeviceExistsAndStateIsAvailable() {
        // Given
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));
        given(deviceRepository.save(any(Device.class))).willReturn(device);

        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setName("New Name");
        updateDTO.setState(DeviceState.IN_USE);

        // When
        Device updatedDevice = deviceService.updateDevice(deviceId, updateDTO);

        // Then
        then(deviceRepository).should().findById(deviceId);
        then(deviceRepository).should().save(deviceCaptor.capture());
        Device capturedDevice = deviceCaptor.getValue();

        assertThat(capturedDevice.getName()).isEqualTo(updateDTO.getName());
        assertThat(capturedDevice.getBrand()).isEqualTo(device.getBrand());
        assertThat(capturedDevice.getState()).isEqualTo(updateDTO.getState());
        assertThat(capturedDevice.getId()).isEqualTo(device.getId());
    }

    @Test
    @DisplayName("Update device should throw InvalidDeviceStateException when updating name of IN_USE device")
    void updateDevice_ShouldThrowException_WhenUpdatingNameOfInUseDevice() {
        // Given
        device.setState(DeviceState.IN_USE);
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setName("New Name");

        // When / Then
        assertThatThrownBy(() -> deviceService.updateDevice(deviceId, updateDTO))
                .isInstanceOf(InvalidDeviceStateException.class)
                .hasMessage("Cannot update name when device is IN_USE");

        then(deviceRepository).should().findById(deviceId);
        then(deviceRepository).should(never()).save(any(Device.class));
    }

    @Test
    @DisplayName("Update device should throw InvalidDeviceStateException when updating brand of IN_USE device")
    void updateDevice_ShouldThrowException_WhenUpdatingBrandOfInUseDevice() {
        // Given
        device.setState(DeviceState.IN_USE);
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setBrand("New Brand");

        // When / Then
        assertThatThrownBy(() -> deviceService.updateDevice(deviceId, updateDTO))
                .isInstanceOf(InvalidDeviceStateException.class)
                .hasMessage("Cannot update brand when device is IN_USE");

        then(deviceRepository).should().findById(deviceId);
        then(deviceRepository).should(never()).save(any(Device.class));
    }

    @Test
    @DisplayName("Update device should update state when state is IN_USE")
    void updateDevice_ShouldAllowStateUpdate_EvenIfInUse() {
        // Given
        device.setState(DeviceState.IN_USE);
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));
        given(deviceRepository.save(any(Device.class))).willReturn(device);

        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setState(DeviceState.INACTIVE);

        // When
        Device updatedDevice = deviceService.updateDevice(deviceId, updateDTO);

        // Then
        assertThat(updatedDevice.getState()).isEqualTo(DeviceState.INACTIVE);
        then(deviceRepository).should().save(device);
    }

    @Test
    @DisplayName("Delete device should delete when device is not IN_USE")
    void deleteDevice_ShouldDelete_WhenDeviceIsNotInUse() {
        // Given
        device.setState(DeviceState.AVAILABLE);
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        // When
        deviceService.deleteDevice(deviceId);

        // Then
        then(deviceRepository).should().findById(deviceId);
        then(deviceRepository).should().delete(device);
    }

    @Test
    @DisplayName("Delete device should throw InvalidDeviceStateException when device is IN_USE")
    void deleteDevice_ShouldThrowException_WhenDeviceIsInUse() {
        // Given
        device.setState(DeviceState.IN_USE);
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        // When / Then
        assertThatThrownBy(() -> deviceService.deleteDevice(deviceId))
                .isInstanceOf(InvalidDeviceStateException.class)
                .hasMessage("Cannot delete device when it is IN_USE");
        
        then(deviceRepository).should(never()).delete(any(Device.class));
    }
}
