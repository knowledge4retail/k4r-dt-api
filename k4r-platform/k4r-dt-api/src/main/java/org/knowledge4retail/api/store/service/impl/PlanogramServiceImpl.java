package org.knowledge4retail.api.store.service.impl;

import com.azure.storage.blob.implementation.models.StorageErrorException;
import com.azure.storage.blob.models.BlobStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.service.BlobStorageService;
import org.knowledge4retail.api.store.converter.PlanogramConverter;
import org.knowledge4retail.api.store.dto.PlanogramDto;
import org.knowledge4retail.api.store.dto.PlanogramMessageDto;
import org.knowledge4retail.api.store.exception.PlanogramNotFoundException;
import org.knowledge4retail.api.store.exception.PlanogramUploadFailedException;
import org.knowledge4retail.api.store.model.Planogram;
import org.knowledge4retail.api.store.repository.PlanogramRepository;
import org.knowledge4retail.api.store.service.PlanogramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class PlanogramServiceImpl implements PlanogramService {

    private final PlanogramRepository repository;
    private final DefaultProducer producer;
    private final BlobStorageService blobStorageService;

    @Value("${azure.blobstorage.container.planograms}")
    private String planogramContainer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.planogram}")
    private String kafkaTopic;

    @Override
    public Planogram create(PlanogramDto planogramDto) {

        String fileName = planogramDto.getStoreId().toString() + "_" + planogramDto.getReferenceId() + "." + planogramDto.getDataFormat();

        String blobUrl;
        try {

            blobUrl = blobStorageService.saveBlob(planogramContainer, fileName, planogramDto.getPlanogram());
        } catch (BlobStorageException | StorageErrorException | IOException e) {

            log.warn(String.format("Error uploading data stream for Planogram of store: %s", planogramDto.getStoreId()), e);
            throw new PlanogramUploadFailedException();
        }

        Planogram planogram = PlanogramConverter.INSTANCE.dtoToPlanogram(planogramDto);
        planogram.setBlobUrl(blobUrl);
        Planogram createdPlanogram = repository.save(planogram);

        PlanogramMessageDto createdDto = PlanogramConverter.INSTANCE.planogramToMessageDto(createdPlanogram);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdPlanogram;
    }

    @Override
    public List<Planogram> readAll(Integer storeId) {

        return repository.findByStoreId(storeId);
    }


    @Override
    public Planogram readById(Integer planogramId) {

        Optional<Planogram> planogram = repository.findById(planogramId);

        if (planogram.isEmpty())
        {
            log.warn(String.format("Plangram with the Id: %d could not be found", planogramId));
            throw new PlanogramNotFoundException();
        }

        return planogram.get();
    }

    @Override
    public byte[] getPlanogramFile(String blobUrl) {

        String blobName = getBlobName(blobUrl);
        InputStream inputStream =  blobStorageService.getBlob(planogramContainer, blobName);

        byte[] bytes;
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "Planogram not found".getBytes();
        }
        return bytes;
    }

    private String getBlobName(String blobUrl) {

        if (!blobUrl.contains("/"))
        {
            throw new PlanogramNotFoundException();
        }
        return blobUrl.substring(blobUrl.lastIndexOf("/")+1);
    }
}
