package com.example.devicemanager.repository;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.domain.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
}
