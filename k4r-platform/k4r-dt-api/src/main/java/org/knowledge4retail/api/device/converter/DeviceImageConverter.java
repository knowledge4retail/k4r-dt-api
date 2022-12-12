package org.knowledge4retail.api.device.converter;

import org.knowledge4retail.api.device.dto.DeviceImageDto;
import org.knowledge4retail.api.device.dto.DeviceImageMessageDto;
import org.knowledge4retail.api.device.model.DeviceImage;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceImageConverter {

    DeviceImageConverter INSTANCE = Mappers.getMapper((DeviceImageConverter.class));


    @Mapping(source = "dto.rosMetadata.frameId", target = "frameId")
    @Mapping(source = "dto.rosMetadata.timestamp", target = "timestamp")
    @Mapping(source = "dto.rosMetadata.deviceId", target = "deviceId")
    @Mapping(source = "dto.devicePose.orientation.x", target = "deviceOrientationX")
    @Mapping(source = "dto.devicePose.orientation.y", target = "deviceOrientationY")
    @Mapping(source = "dto.devicePose.orientation.z", target = "deviceOrientationZ")
    @Mapping(source = "dto.devicePose.orientation.w", target = "deviceOrientationW")
    @Mapping(source = "dto.devicePose.position.x", target = "devicePositionX")
    @Mapping(source = "dto.devicePose.position.y", target = "devicePositionY")
    @Mapping(source = "dto.devicePose.position.z", target = "devicePositionZ")
    @Mapping(source = "dto.cameraPose.orientation.x", target = "cameraOrientationX")
    @Mapping(source = "dto.cameraPose.orientation.y", target = "cameraOrientationY")
    @Mapping(source = "dto.cameraPose.orientation.z", target = "cameraOrientationZ")
    @Mapping(source = "dto.cameraPose.orientation.w", target = "cameraOrientationW")
    @Mapping(source = "dto.cameraPose.position.x", target = "cameraPositionX")
    @Mapping(source = "dto.cameraPose.position.y", target = "cameraPositionY")
    @Mapping(source = "dto.cameraPose.position.z", target = "cameraPositionZ")
    @Mapping(target = "id", ignore = true)
    DeviceImage dtoToDeviceImage(DeviceImageDto dto);


    @Mapping(source = "timestamp",          target = "rosMetadata.timestamp")
    @Mapping(source = "frameId",        target = "rosMetadata.frameId")
    @Mapping(source = "deviceId",           target = "rosMetadata.deviceId")
    @Mapping(source = "deviceOrientationX", target = "devicePose.orientation.x")
    @Mapping(source = "deviceOrientationY", target = "devicePose.orientation.y")
    @Mapping(source = "deviceOrientationZ", target = "devicePose.orientation.z")
    @Mapping(source = "deviceOrientationW", target = "devicePose.orientation.w")
    @Mapping(source = "devicePositionX",    target = "devicePose.position.x")
    @Mapping(source = "devicePositionY",    target = "devicePose.position.y")
    @Mapping(source = "devicePositionZ",    target = "devicePose.position.z")
    @Mapping(source = "cameraOrientationX", target = "cameraPose.orientation.x")
    @Mapping(source = "cameraOrientationY", target = "cameraPose.orientation.y")
    @Mapping(source = "cameraOrientationZ", target = "cameraPose.orientation.z")
    @Mapping(source = "cameraOrientationW", target = "cameraPose.orientation.w")
    @Mapping(source = "cameraPositionX",    target = "cameraPose.position.x")
    @Mapping(source = "cameraPositionY",    target = "cameraPose.position.y")
    @Mapping(source = "cameraPositionZ",    target = "cameraPose.position.z")
    @Named("deviceImageToDto")
    DeviceImageDto deviceImageToDto(DeviceImage deviceImage);

    @IterableMapping(qualifiedByName = "deviceImageToDto")
//    @Named("deviceImageToDto")
    List<DeviceImageDto> deviceImagesToDto(List<DeviceImage> deviceImages);

    DeviceImageMessageDto deviceImageDtoToMessageDto(DeviceImageDto dto);

}
