package org.knowledge4retail.api.service.impl;


import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.device.converter.Map2dConverter;
import org.knowledge4retail.api.device.dto.ros.*;
import org.knowledge4retail.api.device.model.Map2d;
import org.knowledge4retail.api.device.service.impl.Map2dServiceImpl;
import org.knowledge4retail.api.device.dto.Map2dDto;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.device.repository.Map2dRepository;
import org.knowledge4retail.api.device.service.Map2dService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class Map2dServiceImplTest {

    @Mock
    private Map2dRepository repository;
    @Mock
    private Map2dConverter converter;
    @Mock
    private DefaultProducer producer;

    private Map2dService map2dService;

    private Map2dDto mapDto;


    @BeforeEach
    public void setUp() {
        Orientation orientation = Orientation.builder().x(1.0).y(1.0).w(1.0).z(1.0).build();
        Position position = Position.builder().x(1.0).y(1.0).z(1.0).build();
        Pose pose = Pose.builder().orientation(orientation).position(position).build();
        RosMetadata rosMetadata = RosMetadata.builder().frameId("55").timestamp(484390980L).build();
        this.mapDto = Map2dDto.builder().height(15.0).width(15.0).pose(pose).rosMetadata(rosMetadata).resolution(0.99).data(new int[]{-1, 3, 4}).build();

        map2dService = new Map2dServiceImpl(converter, repository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        map2dService.create(1, new Map2dDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void saveMap2DConvertsDtoToModel() {
        //
        int storeId = 1;
        when(repository.findByStoreId(storeId)).thenReturn(any(Map2d.class));

        //
        this.map2dService.create(storeId, mapDto);
        //
        verify(converter).dtoToModel(mapDto);
    }

    @Test
    public void saveMap2DReturnsDto() {
        //
        int storeId = 1;
        Map2d map2d = new Map2d();
        when(repository.save(any())).thenReturn(map2d);
        when(converter.modelToDto(any())).thenReturn(mapDto);
        when(repository.findByStoreId(storeId)).thenReturn(any(Map2d.class));
        //

        Object returnObject = this.map2dService.create(storeId, mapDto);

        //
        MatcherAssert.assertThat(returnObject, instanceOf(Map2dDto.class));
    }

    @Test
    public void saveMap2DCallsConverter() {
        //
        int storeId = 1;
        Map2d map2d = new Map2d();
        when(repository.save(any())).thenReturn(map2d);
        when(converter.modelToDto(any())).thenReturn(any(Map2dDto.class));

        //
        this.map2dService.create(storeId, mapDto);

        //
        verify(converter).modelToDto(map2d);
    }

    @Test
    public void saveMap2DCallsRepository() {
        //
        int storeId = 1;
        when(repository.findByStoreId(storeId)).thenReturn(any(Map2d.class));
        //
        this.map2dService.create(storeId, mapDto);
        //
        verify(repository).save(any());
    }

    @Test
    public void saveMap2DUpdatesMapIfMapWithGivenStoreIdAlreadyExists() {
        //Arrange

        //This Map DTO should be handled by the service. It gets a shopId assigned in SaveMap2D
        int storeId = 5;
        Map2dDto dto = new Map2dDto();

        //This map with shopId 5 and DB-ID 66 already exists in the database
        Map2d existingMap2dInRepo = new Map2d();
        existingMap2dInRepo.setId(66);
        existingMap2dInRepo.setStoreId(storeId);

        //This is a fictional object with an arbitrary DB-ID. The ID is only set to verify that it gets discarded later.
        Map2d objectToBeSaved = new Map2d();
        objectToBeSaved.setId(84374303);
        objectToBeSaved.setStoreId(storeId);

        // simulates Db-Lookup and finds the map with id 66 and shopid 5
        when(repository.findByStoreId(storeId)).thenReturn(existingMap2dInRepo);

        // this is to ensure the service function uses the fictional object further down the road.
        when(converter.dtoToModel(any())).thenReturn(objectToBeSaved);

        // Act
        this.map2dService.create(storeId, dto);

        // Assert
        verify(repository).findByStoreId(storeId);
        verify(repository).save(objectToBeSaved);
        // check that fictional object now also has DB-ID 66.
        Assertions.assertEquals(existingMap2dInRepo.getId(),objectToBeSaved.getId());
    }
}