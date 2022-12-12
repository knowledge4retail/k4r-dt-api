package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.knowledge4retail.api.wireframe.dto.WireframeDto;
import org.knowledge4retail.api.wireframe.exception.WireframeNotFoundException;
import org.knowledge4retail.api.wireframe.exception.WireframeUploadFailedException;
import org.knowledge4retail.api.wireframe.repository.WireframeRepository;
import org.knowledge4retail.api.wireframe.service.impl.WireframeServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WireframeServiceImplTest {

    @Mock
    WireframeRepository WireframeRepository;
    @Mock
    DefaultProducer producer;
    @Mock
    BlobStorageService blobStorageService;

    WireframeServiceImpl service;


    private final String DEFAULT_CONTAINER_NAME = "testContainer";

    @BeforeEach
    public void setUp() {

        service = new WireframeServiceImpl(WireframeRepository, producer, blobStorageService);
        ReflectionTestUtils.setField(service, "WireframeContainer", DEFAULT_CONTAINER_NAME);
    }

    @Test
    public void createSendsMessageToKafka() throws IOException {

        WireframeDto wireframeDto = new WireframeDto();
        wireframeDto.setGTIN("xxxyyxxxyyy");
        wireframeDto.setWireframe(new byte[] { 1,3,-5});
        when(blobStorageService.saveBlob(any(),any(),any())).thenReturn("url://");

        service.create(wireframeDto);

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void saveWireframeCallsSaveOnBlobAndObject() throws IOException {

        WireframeDto wireframeDto = new WireframeDto();
        wireframeDto.setGTIN("xxxyyxxxyyy");
        wireframeDto.setDataFormat("csv");
        wireframeDto.setWireframe(new byte[] { 1,3,-5});
        when(blobStorageService.saveBlob(any(),any(),any())).thenReturn("url://");

        service.create(wireframeDto);

        verify(blobStorageService).saveBlob(DEFAULT_CONTAINER_NAME,  wireframeDto.getGTIN() + "." + wireframeDto.getDataFormat(), wireframeDto.getWireframe());

        verify(WireframeRepository).save(any());
    }


    @Test
    public void saveImageThrowsImageUploadFailedWhenBlobUrlIsEmpty() throws IOException {

        WireframeDto wireframeDto = new WireframeDto();
        wireframeDto.setGTIN("xxxyyxxxyyy");
        wireframeDto.setWireframe(new byte[] { 1,3,-5});
        when(blobStorageService.saveBlob(any(),any(),any())).thenThrow(new IOException());


        Assertions.assertThrows(WireframeUploadFailedException.class, () -> service.create(wireframeDto));

    }


    @Test()
    public void WireframeWithGivenIdNotFoundReturnsAnException() {

        String idNotExistingWireframe = "9999";
        when(WireframeRepository.findBygTIN(idNotExistingWireframe)).thenReturn(Optional.empty());


        Assertions.assertThrows(WireframeNotFoundException.class, () -> service.readById(idNotExistingWireframe));
    }
}
