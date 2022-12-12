package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.StoreCharacteristicConverter;
import org.knowledge4retail.api.store.dto.StoreCharacteristicDto;
import org.knowledge4retail.api.store.model.StoreCharacteristic;
import org.knowledge4retail.api.store.repository.StoreCharacteristicRepository;
import org.knowledge4retail.api.store.service.StoreCharacteristicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StoreCharacteristicServiceImpl implements StoreCharacteristicService {

    private final StoreCharacteristicRepository storeCharacteristicRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.storecharacteristic}")
    private String kafkaTopic;

    public StoreCharacteristicServiceImpl(StoreCharacteristicRepository storeCharacteristicRepository, DefaultProducer producer){

        this.storeCharacteristicRepository = storeCharacteristicRepository;
        this.producer = producer;
    }

    @Override
    public List<StoreCharacteristicDto> readAll() {

        return StoreCharacteristicConverter.INSTANCE.characteristicsToDtos(storeCharacteristicRepository.findAll());
    }

    @Override
    public StoreCharacteristicDto create(StoreCharacteristicDto storeCharacteristicDto) {

        storeCharacteristicDto.setId(null);
        StoreCharacteristic characterToSave = StoreCharacteristicConverter.INSTANCE.dtoToCharacteristic(storeCharacteristicDto);
        StoreCharacteristic savedCharacter = storeCharacteristicRepository.save(characterToSave);

        StoreCharacteristicDto createdDto = StoreCharacteristicConverter.INSTANCE.characteristicToDto(savedCharacter);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        StoreCharacteristicDto deletedDto = StoreCharacteristicConverter.INSTANCE.characteristicToDto(storeCharacteristicRepository.getReferenceById(id));
        storeCharacteristicRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return storeCharacteristicRepository.existsById(id);
    }
}
