package org.knowledge4retail.api.device.converter;

import org.knowledge4retail.api.device.dto.DeviceStatusDto;
import org.knowledge4retail.api.device.model.DeviceStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceStatusConverter  {

    DeviceStatusConverter INSTANCE = Mappers.getMapper(DeviceStatusConverter.class);

    @Mapping(source = "dto.pose.orientation.x", target = "orientationX")
    @Mapping(source = "dto.pose.orientation.y", target = "orientationY")
    @Mapping(source = "dto.pose.orientation.z", target = "orientationZ")
    @Mapping(source = "dto.pose.orientation.w", target = "orientationW")
    @Mapping(source = "dto.pose.position.x", target = "positionX")
    @Mapping(source = "dto.pose.position.y", target = "positionY")
    @Mapping(source = "dto.pose.position.z", target = "positionZ")
    @Mapping(source = "dto.rosMetadata.frameId", target = "frameId")
    @Mapping(source = "dto.rosMetadata.timestamp", target = "timestamp")
    @Mapping(source = "dto.rosMetadata.deviceId", target = "deviceId")
    DeviceStatus dtoToDeviceStatus(DeviceStatusDto dto);

    @Mapping(source = "orientationX", target = "pose.orientation.x")
    @Mapping(source = "orientationY", target = "pose.orientation.y")
    @Mapping(source = "orientationZ", target = "pose.orientation.z")
    @Mapping(source = "orientationW", target = "pose.orientation.w")
    @Mapping(source = "positionX", target = "pose.position.x")
    @Mapping(source = "positionY", target = "pose.position.y")
    @Mapping(source = "positionZ", target = "pose.position.z")
    @Mapping(source = "frameId", target = "rosMetadata.frameId")
    @Mapping(source = "timestamp", target = "rosMetadata.timestamp")
    @Mapping(source = "deviceId", target = "rosMetadata.deviceId")
    @Named("deviceStatusToDto")
    DeviceStatusDto deviceStatusToDto(DeviceStatus deviceStatus);

    @IterableMapping(qualifiedByName = "deviceStatusToDto")
    List<DeviceStatusDto> deviceStatusesToDtos(List<DeviceStatus> deviceStatuses);
}
