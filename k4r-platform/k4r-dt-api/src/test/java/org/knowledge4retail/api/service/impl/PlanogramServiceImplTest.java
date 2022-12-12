package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.knowledge4retail.api.store.dto.PlanogramDto;
import org.knowledge4retail.api.store.exception.PlanogramNotFoundException;
import org.knowledge4retail.api.store.exception.PlanogramUploadFailedException;
import org.knowledge4retail.api.store.repository.PlanogramRepository;
import org.knowledge4retail.api.store.service.impl.PlanogramServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanogramServiceImplTest {

    @Mock
    PlanogramRepository planogramRepository;
    @Mock
    DefaultProducer producer;
    @Mock
    BlobStorageService blobStorageService;

    PlanogramServiceImpl service;


    private final String DEFAULT_CONTAINER_NAME = "testContainer";

    @BeforeEach
    public void setUp() {

        service = new PlanogramServiceImpl(planogramRepository, producer, blobStorageService);
        ReflectionTestUtils.setField(service, "planogramContainer", DEFAULT_CONTAINER_NAME);
    }

    @Test
    public void createSendsMessageToKafka() throws IOException {

        PlanogramDto planogramDto = new PlanogramDto();
        planogramDto.setStoreId(1000);
        planogramDto.setReferenceId("version_12_2021");
        planogramDto.setPlanogram(new byte[] { 1,3,-5});
        when(blobStorageService.saveBlob(any(),any(),any())).thenReturn("url://");

        service.create(planogramDto);

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void savePlanogramCallsSaveOnBlobAndObject() throws IOException {

        PlanogramDto planogramDto = new PlanogramDto();
        planogramDto.setStoreId(1000);
        planogramDto.setReferenceId("version_12_2021");
        planogramDto.setDataFormat("csv");
        planogramDto.setPlanogram(new byte[] { 1,3,-5});
        when(blobStorageService.saveBlob(any(),any(),any())).thenReturn("url://");

        service.create(planogramDto);

        verify(blobStorageService).saveBlob(DEFAULT_CONTAINER_NAME, planogramDto.getStoreId().toString() + "_" + planogramDto.getReferenceId() + "." + planogramDto.getDataFormat(), planogramDto.getPlanogram());

        verify(planogramRepository).save(any());
    }


    @Test
    public void saveImageThrowsImageUploadFailedWhenBlobUrlIsEmpty() throws IOException {

        PlanogramDto planogramDto = new PlanogramDto();
        planogramDto.setStoreId(1000);
        planogramDto.setReferenceId("version_12_2021");
        planogramDto.setPlanogram(new byte[] { 1,3,-5});
        when(blobStorageService.saveBlob(any(),any(),any())).thenThrow(new IOException());


        Assertions.assertThrows(PlanogramUploadFailedException.class, () -> service.create(planogramDto));

    }


    @Test()
    public void PlanogramWithGivenIdNotFoundReturnsAnException() {

        Integer idNotExistingPlanogram = 9999;
        when(planogramRepository.findById(idNotExistingPlanogram)).thenReturn(Optional.empty());


        Assertions.assertThrows(PlanogramNotFoundException.class, () -> service.readById(idNotExistingPlanogram));
    }
}
