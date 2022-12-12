package org.knowledge4retail.api.device.converter.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.converter.Map2dConverter;
import org.knowledge4retail.api.device.dto.Map2dDto;
import org.knowledge4retail.api.device.dto.Map2dMessageDto;
import org.knowledge4retail.api.device.dto.ros.RosMetadata;
import org.knowledge4retail.api.device.dto.ros.Orientation;
import org.knowledge4retail.api.device.dto.ros.Pose;
import org.knowledge4retail.api.device.dto.ros.Position;
import org.knowledge4retail.api.device.model.Map2d;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

@Slf4j
@Component
public class Map2dConverterImpl implements Map2dConverter {
    @Override
    public Map2d dtoToModel(Map2dDto map2dDto) {
        return Map2d.builder().timestamp(map2dDto.getRosMetadata().getTimestamp())
                .frameId(map2dDto.getRosMetadata().getFrameId())
                .deviceId(map2dDto.getRosMetadata().getDeviceId())
                .resolution(map2dDto.getResolution())
                .height(map2dDto.getHeight())
                .width(map2dDto.getWidth())
                .lengthUnitId(map2dDto.getLengthUnitId())
                .positionX(map2dDto.getPose().getPosition().getX())
                .positionY(map2dDto.getPose().getPosition().getY())
                .positionZ(map2dDto.getPose().getPosition().getZ())
                .orientationX(map2dDto.getPose().getOrientation().getX())
                .orientationY(map2dDto.getPose().getOrientation().getY())
                .orientationZ(map2dDto.getPose().getOrientation().getZ())
                .orientationW(map2dDto.getPose().getOrientation().getW())
                .data(this.convertDataFieldFromDtoToModelFormat(map2dDto.getData()))
                .storeId(map2dDto.getStoreId()).build();
    }

    @Override
    public Map2dDto modelToDto(Map2d map2dEntity) {
        return Map2dDto.builder()
                .id(map2dEntity.getId())
                .rosMetadata(RosMetadata.builder()
                    .timestamp(map2dEntity.getTimestamp())
                    .frameId(map2dEntity.getFrameId()).build())
                .resolution(map2dEntity.getResolution())
                .height(map2dEntity.getHeight())
                .width(map2dEntity.getWidth())
                .lengthUnitId(map2dEntity.getLengthUnitId())
                .pose(Pose.builder()
                        .orientation(
                                Orientation.builder()
                                    .x(map2dEntity.getOrientationX())
                                    .y(map2dEntity.getOrientationY())
                                    .z(map2dEntity.getOrientationZ())
                                    .w(map2dEntity.getOrientationW()).build())
                        .position(
                                Position.builder()
                                        .x(map2dEntity.getPositionX())
                                        .y(map2dEntity.getPositionY())
                                        .z(map2dEntity.getPositionZ()).build())
                         .build()
                        )
                .storeId(map2dEntity.getStoreId())
                .data(this.convertToIntArray(map2dEntity.getData()))
                        .build();

    }

    @Override
    public Map2dMessageDto dtoToMessageDto(Map2dDto dto) {
        return Map2dMessageDto.builder()
                .id(dto.getId())
                .rosMetadata(dto.getRosMetadata())
                .resolution(dto.getResolution())
                .width(dto.getWidth())
                .height(dto.getHeight())
                .lengthUnitId(dto.getLengthUnitId())
                .pose(dto.getPose())
                .storeId(dto.getStoreId())
                .build();
    }

    private int[] convertToIntArray(String data) {
        if (data.isEmpty())
            throw new IllegalArgumentException("Data string may not be empty!");

        return Stream.of(removeUnwantedCharacters(data).split(",")).mapToInt(Integer::parseInt).toArray();
    }

    private String convertDataFieldFromDtoToModelFormat(int[] inputData) {
        String result = Arrays.toString(inputData);
        result = this.removeUnwantedCharacters(result);
        return result;
    }
    private String removeUnwantedCharacters(String s)
    {
        return s.replace(" ","").replace("[","").replace("]","");
    }
}
