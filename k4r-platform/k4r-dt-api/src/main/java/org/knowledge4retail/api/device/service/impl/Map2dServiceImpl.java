package org.knowledge4retail.api.device.service.impl;

import lombok.RequiredArgsConstructor;
import org.knowledge4retail.api.device.converter.Map2dConverter;
import org.knowledge4retail.api.device.dto.Map2dDto;
import org.knowledge4retail.api.device.model.Map2d;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.device.repository.Map2dRepository;
import org.knowledge4retail.api.device.service.Map2dService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Map2dServiceImpl implements Map2dService {

    private final Map2dConverter converter;
    private final Map2dRepository repository;
    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.map2d}")
    private String kafkaTopic;


    @Override
    public Map2dDto create(Integer storeId, Map2dDto map2dDto) {

        map2dDto.setStoreId(storeId);

        Map2d map2d = converter.dtoToModel(map2dDto);

        Map2d mapInRepo = repository.findByStoreId(storeId);

        if (mapInRepo != null)
            map2d.setId(mapInRepo.getId());

        Map2dDto createdDto = converter.modelToDto(repository.save(map2d));

        producer.publishCreate(kafkaTopic, converter.dtoToMessageDto(createdDto));
        return createdDto;
    }

    @Override
    public Map2dDto readByStoreId(Integer storeId) {
        return converter.modelToDto(repository.findByStoreId(storeId));
    }

    @Override
    public boolean exists(Integer storeId) {
        return repository.existsByStoreId(storeId);
    }
    
}
