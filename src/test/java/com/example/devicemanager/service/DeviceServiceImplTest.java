package com.example.devicemanager.service;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceCreateDTO;
import com.example.devicemanager.dto.DeviceResponseDTO;
import com.example.devicemanager.dto.DeviceUpdateDTO;
import com.example.devicemanager.exception.InvalidDeviceStateException;
import com.example.devicemanager.mapper.DeviceMapper;
import com.example.devicemanager.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Spy
    private DeviceMapper deviceMapper = Mappers.getMapper(DeviceMapper.class);

    @InjectMocks
    private DeviceServiceImpl deviceService;

    @Captor
    private ArgumentCaptor<Device> deviceCaptor;

    private Device device;
    private DeviceCreateDTO deviceCreateDTO;
    private UUID deviceId;

    @BeforeEach
    void setUp() {
        deviceId = UUID.randomUUID();
        device = new Device("Test Device", "Test Brand", DeviceState.AVAILABLE);
        
        deviceCreateDTO = new DeviceCreateDTO("Test Device", "Test Brand", DeviceState.AVAILABLE);
    }

    @Test
    @DisplayName("Create device should save and return the device DTO")
    void createDevice_ShouldReturnSavedDevice() {
        // Given
        given(deviceRepository.save(any(Device.class))).willReturn(device);

        // When
        DeviceResponseDTO createdDevice = deviceService.createDevice(deviceCreateDTO);

        // Then
        then(deviceRepository).should().save(deviceCaptor.capture());
        Device capturedDevice = deviceCaptor.getValue();

        assertThat(capturedDevice.getName()).isEqualTo(deviceCreateDTO.name());
        assertThat(capturedDevice.getBrand()).isEqualTo(deviceCreateDTO.brand());
        assertThat(capturedDevice.getState()).isEqualTo(deviceCreateDTO.state());

        assertThat(createdDevice.name()).isEqualTo(device.getName());
        assertThat(createdDevice.brand()).isEqualTo(device.getBrand());
        assertThat(createdDevice.state()).isEqualTo(device.getState());
    }

    @Test
    @DisplayName("Get all devices should return a list of device DTOs")
    void getAllDevices_ShouldReturnListOfDevices() {
        // Given
        given(deviceRepository.findAll()).willReturn(List.of(device));

        // When
        List<DeviceResponseDTO> devices = deviceService.getAllDevices();

        // Then
        assertThat(devices).isNotEmpty().hasSize(1);
        assertThat(devices.getFirst().name()).isEqualTo(device.getName());
        then(deviceRepository).should().findAll();
    }

    @Test
    @DisplayName("Get device by ID should return device DTO when found")
    void getDeviceById_ShouldReturnDevice_WhenFound() {
        // Given
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        // When
        DeviceResponseDTO foundDevice = deviceService.getDeviceById(deviceId);

        // Then
        assertThat(foundDevice).isNotNull();
        assertThat(foundDevice.name()).isEqualTo(device.getName());
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
    @DisplayName("Get devices by brand should return list of device DTOs")
    void getDevicesByBrand_ShouldReturnListOfDevices() {
        // Given
        String brand = "Test Brand";
        given(deviceRepository.findByBrand(brand)).willReturn(List.of(device));

        // When
        List<DeviceResponseDTO> devices = deviceService.getDevicesByBrand(brand);

        // Then
        assertThat(devices).isNotEmpty().hasSize(1);
        assertThat(devices.getFirst().brand()).isEqualTo(brand);
        then(deviceRepository).should().findByBrand(brand);
    }

    @Test
    @DisplayName("Get devices by state should return list of device DTOs")
    void getDevicesByState_ShouldReturnListOfDevices() {
        // Given
        DeviceState state = DeviceState.AVAILABLE;
        given(deviceRepository.findByState(state)).willReturn(List.of(device));

        // When
        List<DeviceResponseDTO> devices = deviceService.getDevicesByState(state);

        // Then
        assertThat(devices).isNotEmpty().hasSize(1);
        assertThat(devices.getFirst().state()).isEqualTo(state);
        then(deviceRepository).should().findByState(state);
    }

    @Test
    @DisplayName("Update device should update fields when device exists and is AVAILABLE")
    void updateDevice_ShouldUpdateFields_WhenDeviceExistsAndStateIsAvailable() {
        // Given
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));
        given(deviceRepository.save(any(Device.class))).willReturn(device);

        DeviceUpdateDTO updateDTO = new DeviceUpdateDTO("Updated Name", null, DeviceState.IN_USE);

        // When
        DeviceResponseDTO updatedDevice = deviceService.updateDevice(deviceId, updateDTO);

        // Then
        then(deviceRepository).should().findById(deviceId);
        then(deviceRepository).should().save(deviceCaptor.capture());
        Device capturedDevice = deviceCaptor.getValue();

        assertThat(capturedDevice.getName()).isEqualTo(updateDTO.name());
        assertThat(capturedDevice.getBrand()).isEqualTo(device.getBrand());
        assertThat(capturedDevice.getState()).isEqualTo(updateDTO.state());
        assertThat(capturedDevice.getId()).isEqualTo(device.getId());
        
        assertThat(updatedDevice.name()).isEqualTo(updateDTO.name());
    }

    @Test
    @DisplayName("Update device should throw InvalidDeviceStateException when updating name of IN_USE device")
    void updateDevice_ShouldThrowException_WhenUpdatingNameOfInUseDevice() {
        // Given
        device.setState(DeviceState.IN_USE);
        given(deviceRepository.findById(deviceId)).willReturn(Optional.of(device));

        DeviceUpdateDTO updateDTO = new DeviceUpdateDTO("New Name", null, null);

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

        DeviceUpdateDTO updateDTO = new DeviceUpdateDTO(null, "New Brand", null);

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

        DeviceUpdateDTO updateDTO = new DeviceUpdateDTO(null, null, DeviceState.INACTIVE);

        // When
        DeviceResponseDTO updatedDevice = deviceService.updateDevice(deviceId, updateDTO);

        // Then
        assertThat(updatedDevice.state()).isEqualTo(DeviceState.INACTIVE);
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
