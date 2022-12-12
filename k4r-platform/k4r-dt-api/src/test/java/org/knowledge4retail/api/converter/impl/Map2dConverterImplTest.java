package org.knowledge4retail.api.converter.impl;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.device.converter.Map2dConverter;
import org.knowledge4retail.api.device.converter.impl.Map2dConverterImpl;
import org.knowledge4retail.api.device.dto.Map2dDto;
import org.knowledge4retail.api.device.dto.ros.*;
import org.knowledge4retail.api.device.model.Map2d;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;


public class Map2dConverterImplTest {

    private Map2dDto mapDto;
    private Map2dConverter converter;
    private Map2d map2dEntity;

    @BeforeEach
    public void setUp() {
        Orientation orientation = Orientation.builder().x(1.0).y(1.0).w(1.0).z(1.0).build();
        Position position = Position.builder().x(1.0).y(1.0).z(1.0).build();
        Pose pose = Pose.builder().orientation(orientation).position(position).build();
        String frameId = "55";
        long timestamp = 484390980L;
        RosMetadata rosMetadata = RosMetadata.builder().frameId(frameId).timestamp(timestamp).build();
        double resolution = 0.99;
        int[] data = {-1, 3, 4};
        double height = 15;
        double width = 15;

        this.mapDto = Map2dDto.builder().height(height).width(width).pose(pose).rosMetadata(rosMetadata).resolution(resolution).data(data).storeId(1).build();
        this.converter = new Map2dConverterImpl();
        this.map2dEntity = Map2d.builder()
                .id(1)
                .positionX(position.getX())
                .positionY(position.getY())
                .positionZ(position.getZ())
                .orientationX(orientation.getX())
                .orientationY(orientation.getY())
                .orientationZ(orientation.getZ())
                .orientationW(orientation.getW())
                .resolution(resolution)
                .data("-1,3,4")
                .height(height)
                .width(width)
                .frameId(frameId)
                .timestamp(timestamp)
                .build();

    }

    @Test
    public void dtoToModelDumbValuesRemainUnchanged() {
        Map2d map = converter.dtoToModel(this.mapDto);

        Assertions.assertEquals(this.mapDto.getRosMetadata().getTimestamp(), map.getTimestamp());
        Assertions.assertEquals(this.mapDto.getRosMetadata().getFrameId(), map.getFrameId());
        Assertions.assertEquals(this.mapDto.getRosMetadata().getDeviceId(), map.getDeviceId());

        Assertions.assertEquals(this.mapDto.getResolution(), map.getResolution());
        Assertions.assertEquals(this.mapDto.getWidth(), map.getWidth());
        Assertions.assertEquals(this.mapDto.getHeight(), map.getHeight());

        Assertions.assertEquals(this.mapDto.getPose().getPosition().getX(), map.getPositionX());
        Assertions.assertEquals(this.mapDto.getPose().getPosition().getY(), map.getPositionY());
        Assertions.assertEquals(this.mapDto.getPose().getPosition().getZ(), map.getPositionZ());

        Assertions.assertEquals(this.mapDto.getPose().getOrientation().getX(), map.getOrientationX());
        Assertions.assertEquals(this.mapDto.getPose().getOrientation().getY(), map.getOrientationY());
        Assertions.assertEquals(this.mapDto.getPose().getOrientation().getZ(), map.getOrientationZ());
        Assertions.assertEquals(this.mapDto.getPose().getOrientation().getW(), map.getOrientationW());

        Assertions.assertEquals(this.mapDto.getStoreId(), map.getStoreId());
    }

    @Test
    public void dtoToModelMappingDoesNotMapIdField() {
        this.mapDto.setId(2534);
        Map2d map = converter.dtoToModel(this.mapDto);

        MatcherAssert.assertThat(map.getId(), not(equalTo(mapDto.getId())));
    }

    @Test
    public void dtoToModelMapArrayConvertsProperlyToString() {
        Map2d map = converter.dtoToModel(this.mapDto);

        Assertions.assertEquals(map.getData(), "-1,3,4");
    }

    @Test
    public void modelToDtoDumbValuesRemainUnchanged() {


        Map2dDto map2dDto = converter.modelToDto(map2dEntity);

        Assertions.assertEquals(map2dEntity.getId(), map2dDto.getId());
        Assertions.assertEquals(map2dEntity.getTimestamp(), map2dDto.getRosMetadata().getTimestamp());
        Assertions.assertEquals(map2dEntity.getFrameId(), map2dDto.getRosMetadata().getFrameId());
        Assertions.assertEquals(map2dEntity.getDeviceId(), map2dDto.getRosMetadata().getDeviceId());

        Assertions.assertEquals(map2dEntity.getResolution(), map2dDto.getResolution());
        Assertions.assertEquals(map2dEntity.getWidth(), map2dDto.getWidth());
        Assertions.assertEquals(map2dEntity.getHeight(), map2dDto.getHeight());

        Assertions.assertEquals(map2dEntity.getPositionX(), map2dDto.getPose().getPosition().getX());
        Assertions.assertEquals(map2dEntity.getPositionY(), map2dDto.getPose().getPosition().getY());
        Assertions.assertEquals(map2dEntity.getPositionZ(), map2dDto.getPose().getPosition().getZ());

        Assertions.assertEquals(map2dEntity.getOrientationX(), map2dDto.getPose().getOrientation().getX());
        Assertions.assertEquals(map2dEntity.getOrientationY(), map2dDto.getPose().getOrientation().getY());
        Assertions.assertEquals(map2dEntity.getOrientationZ(), map2dDto.getPose().getOrientation().getZ());
        Assertions.assertEquals(map2dEntity.getOrientationW(), map2dDto.getPose().getOrientation().getW());


    }

    @Test
    public void modelToDtoMapStringToArrayInt() {

        Map2dDto map2dDto = converter.modelToDto(this.map2dEntity);

        Assertions.assertArrayEquals(map2dDto.getData(), new int[]{-1, 3, 4});
    }


}