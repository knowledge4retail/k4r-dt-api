package org.knowledge4retail.api.wireframe.service.impl;

import com.azure.storage.blob.implementation.models.StorageErrorException;
import com.azure.storage.blob.models.BlobStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.knowledge4retail.api.wireframe.converter.WireframeConverter;
import org.knowledge4retail.api.wireframe.dto.WireframeDto;
import org.knowledge4retail.api.wireframe.dto.WireframeMessageDto;
import org.knowledge4retail.api.wireframe.exception.WireframeNotFoundException;
import org.knowledge4retail.api.wireframe.exception.WireframeUploadFailedException;
import org.knowledge4retail.api.wireframe.model.Wireframe;
import org.knowledge4retail.api.wireframe.repository.WireframeRepository;
import org.knowledge4retail.api.wireframe.service.WireframeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class WireframeServiceImpl implements WireframeService {

    private final WireframeRepository repository;
    private final DefaultProducer producer;
    private final BlobStorageService blobStorageService;

    @Value("${azure.blobstorage.container.wireframes}")
    private String WireframeContainer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.wireframe}")
    private String kafkaTopic;

    @Override
    public Wireframe create(WireframeDto WireframeDto) {

        String fileName = WireframeDto.getGTIN() + "." + WireframeDto.getDataFormat();

        String blobUrl;
        try {

            blobUrl = blobStorageService.saveBlob(WireframeContainer, fileName, WireframeDto.getWireframe());
        } catch (BlobStorageException | StorageErrorException | IOException e) {

            log.warn(String.format("Error uploading data stream for Wireframe with GTIN: %s", WireframeDto.getGTIN()), e);
            throw new WireframeUploadFailedException();
        }

        Wireframe Wireframe = WireframeConverter.INSTANCE.dtoToWireframe(WireframeDto);
        Wireframe.setBlobUrl(blobUrl);
        Wireframe createdWireframe = repository.save(Wireframe);

        WireframeMessageDto createdDto = WireframeConverter.INSTANCE.WireframeToMessageDto(createdWireframe);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdWireframe;
    }

    @Override
    public List<Wireframe> readAll() {

        return repository.findAll();
    }


    @Override
    public Wireframe readById(String gTIN) {

        Optional<Wireframe> Wireframe = repository.findBygTIN(gTIN);

        if (Wireframe.isEmpty())
        {
            log.warn(String.format("Wireframe with the GTIN: %s could not be found", gTIN));
            throw new WireframeNotFoundException();
        }

        return Wireframe.get();
    }

    @Override
    public byte[] getWireframeFile(String blobUrl) {

        String blobName = getBlobName(blobUrl);
        InputStream inputStream =  blobStorageService.getBlob(WireframeContainer, blobName);

        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "Wireframe not found".getBytes();
        }
        return bytes;
    }

    private String getBlobName(String blobUrl) {

        if (!blobUrl.contains("/"))
        {
            throw new WireframeNotFoundException();
        }
        return blobUrl.substring(blobUrl.lastIndexOf("/")+1);
    }
}
