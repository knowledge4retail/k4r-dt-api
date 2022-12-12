package org.knowledge4retail.api.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.device.dto.DeviceImageDto;
import org.knowledge4retail.api.device.exception.DeviceImageNotFoundException;
import org.knowledge4retail.api.device.exception.ImageUploadFailedException;
import org.knowledge4retail.api.device.model.DeviceImage;
import org.knowledge4retail.api.device.repository.DeviceImageRepository;
import org.knowledge4retail.api.device.service.impl.DeviceImageServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceImageServiceImplTest {

    @Mock
    private DeviceImageRepository repositoryMock;
    @Mock
    private BlobStorageService blobStorageServiceMock;
    @Mock
    private DefaultProducer producer;

    DeviceImageServiceImpl service;

    private final String DEFAULT_CONTAINER_NAME = "testContainer";


    @BeforeEach
    public void setUp() {

        this.service = new DeviceImageServiceImpl(repositoryMock, blobStorageServiceMock, producer);
        ReflectionTestUtils.setField(service, "deviceImageContainer", DEFAULT_CONTAINER_NAME);
    }

    @Test
    public void createSendsMessageToKafka() throws IOException {

        when(blobStorageServiceMock.saveBlob(any(),any(),any())).thenReturn("url://");

        service.create(new DeviceImageDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void saveImageCallsSaveOnBlobAndObject() throws IOException {

        DeviceImageDto dto = new DeviceImageDto();
        dto.setImageName("testimage.jpg");
        dto.setImage(new byte[] { 1,3,-5});
        when(blobStorageServiceMock.saveBlob(any(),any(),any())).thenReturn("url://");

        this.service.create(dto);

        verify(blobStorageServiceMock).saveBlob(DEFAULT_CONTAINER_NAME, dto.getImageName(), dto.getImage());

        verify(repositoryMock).save(any());

    }

    @Test()
    public void saveImageThrowsImageUploadFailedWhenBlobUrlIsEmpty() throws IOException {

        DeviceImageDto dto = new DeviceImageDto();
        dto.setImageName("testimage.jpg");
        dto.setImage(new byte[] { 1,3,-5});
        when(blobStorageServiceMock.saveBlob(any(),any(),any())).thenThrow(new IOException());


        Assertions.assertThrows(ImageUploadFailedException.class, () -> this.service.create(dto));

    }


    @Test()
    public void ImageWithGivenIdNotFoundReturnsAnException() {

        int idNotExistingImage = 1989;
        when(this.repositoryMock.findById(idNotExistingImage)).thenReturn(Optional.empty());


        Assertions.assertThrows(DeviceImageNotFoundException.class, () -> this.service.read(idNotExistingImage));
    }

    @Test
    public void ImageCallsGetBlobWithTheCorrectBlobName() {

        DeviceImage image = new DeviceImage();
        image.setBlobUrl("http://127.0.0.1:10000/accountname/k4r-device-images/Alfa-Romeo.jpg");

        Optional<DeviceImage> expectedImage = Optional.of(image);

        when(this.repositoryMock.findById(1)).thenReturn(expectedImage);


        this.service.read(1);


        verify(blobStorageServiceMock).getBlob(DEFAULT_CONTAINER_NAME, "Alfa-Romeo.jpg");

    }

}