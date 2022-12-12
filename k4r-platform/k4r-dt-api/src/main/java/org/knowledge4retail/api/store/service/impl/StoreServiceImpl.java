package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.StoreConverter;
import org.knowledge4retail.api.store.dto.StoreAggregateDto;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.store.repository.StoreRepository;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.store}")
    private String kafkaTopic;

    public StoreServiceImpl(StoreRepository storeRepository, DefaultProducer producer){

        this.storeRepository = storeRepository;
        this.producer = producer;
    }

    @Override
    public StoreDto create(StoreDto dto) throws IllegalArgumentException {

        dto.setId(null);
        StoreDto storeDto = StoreConverter.INSTANCE.storeToDto(this.storeRepository.save(StoreConverter.INSTANCE.dtoToStore(dto)));

        producer.publishCreate(kafkaTopic, storeDto);
        return storeDto;
    }

    @Override
    public List<StoreDto> readAll() {

        return StoreConverter.INSTANCE.storesToDtos(storeRepository.findAll());
    }

    @Override
    public StoreDto read(Integer storeId) {

        return StoreConverter.INSTANCE.storeToDto(this.storeRepository.getReferenceById(storeId));
    }

    @Override
    public StoreDto readByExternalReferenceId(String externalReferenceId) {

        return StoreConverter.INSTANCE.storeToDto(storeRepository.findByExternalReferenceId(externalReferenceId));
    }

    @Override
    public StoreDto update(Integer storeId, StoreDto dto) {

        dto.setId(storeId); //in case we receive a request body with a different id than given in path

        StoreDto oldDto = read(storeId);
        StoreDto newDto = StoreConverter.INSTANCE.storeToDto(this.storeRepository.save(StoreConverter.INSTANCE.dtoToStore(dto)));

        producer.publishUpdate(kafkaTopic, newDto, oldDto);
        return newDto;
    }

    @Override
    public void delete(Integer storeId) {

        StoreDto storeDto = read(storeId);
        storeRepository.deleteById(storeId);

        producer.publishDelete(kafkaTopic, storeDto);
    }

    @Override
    public boolean exists(Integer storeId) {

        return storeRepository.existsById(storeId);
    }

    @Override
    public boolean existsByExternalReferenceId(String externalReferenceId) {

        return storeRepository.existsByExternalReferenceId(externalReferenceId);
    }

    @Override
    public List<StoreAggregateDto> readAllAggregates() {

        return StoreConverter.INSTANCE.
                storeAggregatesToDtos(storeRepository.getAllAggregates());
    }
}