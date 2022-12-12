package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.store.model.Store;
import org.knowledge4retail.api.store.repository.StoreRepository;
import org.knowledge4retail.api.store.service.impl.StoreServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StoreServiceImplTest {

    @Mock
    private StoreRepository repository;
    @Mock
    private DefaultProducer producer;

    private StoreServiceImpl service;


    @BeforeEach
    public void setUp() {

        service = new StoreServiceImpl(repository, producer);

        Store store = Store.builder().id(1).storeName("Test").build();
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new StoreDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        service.update(any(), new StoreDto());

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }


    @Test
    public void updateStoreSetsParameterIdToDtoWhenIdsAreDifferent() {

        Integer parameterId = 2;
        Integer objectId = 1;
        StoreDto storeDto = StoreDto.builder().id(objectId).storeName("My Store").storeNumber("MFS001").build();
        service.update(parameterId, storeDto);
        Assertions.assertEquals(parameterId, storeDto.getId());
    }
}