package org.knowledge4retail.api.device.service.impl;

import com.azure.storage.blob.implementation.models.StorageErrorException;
import com.azure.storage.blob.models.BlobStorageException;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.converter.DeviceImageConverter;
import org.knowledge4retail.api.device.dto.DeviceImageDto;
import org.knowledge4retail.api.device.exception.DeviceImageNotFoundException;
import org.knowledge4retail.api.device.exception.ImageUploadFailedException;
import org.knowledge4retail.api.device.model.DeviceImage;
import org.knowledge4retail.api.device.repository.DeviceImageRepository;
import org.knowledge4retail.api.device.service.DeviceImageService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DeviceImageServiceImpl implements DeviceImageService {

    private final DeviceImageRepository repository;

    private final BlobStorageService blobService;

    private final DefaultProducer producer;

    @Value("${azure.blobstorage.container.device.images}")
    private String deviceImageContainer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.deviceimage}")
    private String kafkaTopic;

    public DeviceImageServiceImpl(DeviceImageRepository imageRepository, BlobStorageService blobService, DefaultProducer producer) {
        this.repository = imageRepository;
        this.blobService = blobService;
        this.producer = producer;
    }

    @Override
    public DeviceImage create(DeviceImageDto deviceImageDto) {

        String blobUrl;
        try {
            blobUrl = this.blobService.saveBlob(
                    deviceImageContainer, deviceImageDto.getImageName(), deviceImageDto.getImage());
        } catch (BlobStorageException | StorageErrorException | IOException e) {
            log.warn(String.format("Error uploading data stream for file: %s", deviceImageDto.getImageName()), e);
            throw new ImageUploadFailedException();
        }
        DeviceImage deviceImage = DeviceImageConverter.INSTANCE.dtoToDeviceImage(deviceImageDto);

        deviceImage.setBlobUrl(blobUrl);
        DeviceImage createdImage = repository.save(deviceImage);

        DeviceImageDto createdDto = DeviceImageConverter.INSTANCE.deviceImageToDto(createdImage);
        producer.publishCreate(kafkaTopic, DeviceImageConverter.INSTANCE.deviceImageDtoToMessageDto(createdDto));
        return createdImage;
    }

    @Override
    public List<DeviceImageDto> readByLabelTypeAndLabelId(String labelName, String labelId) {

        return DeviceImageConverter.INSTANCE.deviceImagesToDto(this.repository.findByLabelIdAndLabelName(labelId, labelName));
    }

    @Override
    public InputStream read(int imageId) {


        Optional<DeviceImage> deviceImage = this.repository.findById(imageId);
        
        if (deviceImage.isEmpty())
        {
            log.warn(String.format("Device Image with the Id: %d could not be found", imageId));
            throw new DeviceImageNotFoundException();
        }
        String blobName = getBlobName(deviceImage.get());

        return this.blobService.getBlob(deviceImageContainer, blobName);
    }


    private String getBlobName(DeviceImage deviceImage) {
        String blobUrl =  deviceImage.getBlobUrl();
        if (!blobUrl.contains("/"))
        {
            throw new DeviceImageNotFoundException();
        }
        return blobUrl.substring(blobUrl.lastIndexOf("/")+1);
    }

}
