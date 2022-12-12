package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.StorePropertyConverter;
import org.knowledge4retail.api.store.dto.StorePropertyDto;
import org.knowledge4retail.api.store.model.StoreProperty;
import org.knowledge4retail.api.store.repository.StorePropertyRepository;
import org.knowledge4retail.api.store.service.StorePropertyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StorePropertyServiceImpl implements StorePropertyService {


    @Value("${org.knowledge4retail.api.listener.kafka.topics.storeproperty}")
    private String kafkaTopic;

    private final StorePropertyRepository storePropertyRepository;
    private final DefaultProducer producer;

    public List<StorePropertyDto> readByStoreId(Integer storeId){

        return StorePropertyConverter.INSTANCE.propertiesToDtos(storePropertyRepository.getAllByStore(storeId));
    }

    @Override
    public StorePropertyDto create(StorePropertyDto storePropertyDto) {

        storePropertyDto.setId(null);
        StoreProperty propertyToSave = StorePropertyConverter.INSTANCE.dtoToProperty(storePropertyDto);
        StoreProperty savedProperty = storePropertyRepository.save(propertyToSave);

        StorePropertyDto createdDto = StorePropertyConverter.INSTANCE.propertyToDto(savedProperty);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    @Transactional
    public void delete(Integer storeId, Integer characteristicId) {

        Optional<StoreProperty> propertyOpt = storePropertyRepository.getProperty(storeId, characteristicId);
        if (propertyOpt.isPresent()){

            StorePropertyDto deletedDto = StorePropertyConverter.INSTANCE.propertyToDto(propertyOpt.get());
            storePropertyRepository.deleteProperty(storeId, characteristicId);
            producer.publishDelete(kafkaTopic, deletedDto);
        }
    }

    public boolean exists(Integer storeId, Integer characteristicId){

        return storePropertyRepository.existsByCharacteristicIdAndStoreId(storeId, characteristicId);
    }

}