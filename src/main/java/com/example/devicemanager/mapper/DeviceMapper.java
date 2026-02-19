package com.example.devicemanager.mapper;

import com.example.devicemanager.domain.Device;
import com.example.devicemanager.dto.DeviceCreateDTO;
import com.example.devicemanager.dto.DeviceResponseDTO;
import com.example.devicemanager.dto.DeviceUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    Device toEntity(DeviceCreateDTO deviceCreateDTO);

    DeviceResponseDTO toDTO(Device device);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(DeviceUpdateDTO deviceUpdateDTO, @MappingTarget Device device);
}
