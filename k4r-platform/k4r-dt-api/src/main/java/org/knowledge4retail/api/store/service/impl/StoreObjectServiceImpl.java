package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.StoreObjectConverter;
import org.knowledge4retail.api.store.dto.StoreObjectDto;
import org.knowledge4retail.api.store.model.StoreObject;
import org.knowledge4retail.api.store.repository.StoreObjectRepository;
import org.knowledge4retail.api.store.service.StoreObjectService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class StoreObjectServiceImpl implements StoreObjectService {

    private final StoreObjectRepository repository;
    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.storeobject}")
    private String kafkaTopic;

    public StoreObjectServiceImpl(StoreObjectRepository storeObjectRepository, DefaultProducer defaultProducer, StoreService storeService) {

        this.repository = storeObjectRepository;
        this.producer = defaultProducer;
    }

    @Override
    public StoreObjectDto create(StoreObjectDto dto) {

        dto.setId(null);
        dto.setLocationTimestamp(OffsetDateTime.now());
        StoreObjectDto storeObjectDto = StoreObjectConverter.INSTANCE.storeObjectToDto(
                repository.save(StoreObjectConverter.INSTANCE.dtoToStoreObject(dto)));

        producer.publishCreate(kafkaTopic, storeObjectDto);

        return storeObjectDto;
    }

    @Override
    public List<StoreObjectDto> readByStoreId(Integer storeId) {

        return StoreObjectConverter.INSTANCE.storeObjectsToDtos(
                repository.findByStoreId(storeId));
    }

    @Override
    public List<StoreObjectDto> readByStoreIdAndType(Integer storeId, String type) {

        if (type == null || type.isEmpty() || type.isBlank()) {
            return this.readByStoreId(storeId);
        }

        return StoreObjectConverter.INSTANCE.storeObjectsToDtos(
                repository.findByStoreIdAndType(storeId, type));
    }

    @Override
    public void delete(Integer storeObjectId) {

        StoreObject storeObject = repository.getReferenceById(storeObjectId);
        repository.deleteById(storeObjectId);
        producer.publishDelete(kafkaTopic, StoreObjectConverter.INSTANCE.storeObjectToDto(storeObject));
    }

    @Override
    public boolean exists(Integer storeObjectId) {

        return repository.existsById(storeObjectId);
    }

}
