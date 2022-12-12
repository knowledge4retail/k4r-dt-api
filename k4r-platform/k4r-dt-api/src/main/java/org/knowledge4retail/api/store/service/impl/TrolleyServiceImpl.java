package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.TrolleyConverter;
import org.knowledge4retail.api.store.dto.TrolleyDto;
import org.knowledge4retail.api.store.model.Trolley;
import org.knowledge4retail.api.store.repository.TrolleyRepository;
import org.knowledge4retail.api.store.service.StoreService;
import org.knowledge4retail.api.store.service.TrolleyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrolleyServiceImpl implements TrolleyService {

    private final TrolleyRepository trolleyRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.trolley}")
    private String kafkaTopic;

    public TrolleyServiceImpl(TrolleyRepository trolleyRepository, StoreService storeService, DefaultProducer producer) {

        this.trolleyRepository = trolleyRepository;
        this.producer = producer;
    }

    @Override
    public List<TrolleyDto> readByStoreId(Integer storeId) {

        return TrolleyConverter.INSTANCE.trolleysToDtos(trolleyRepository.findByStoreId(storeId));
    }

    @Override
    public TrolleyDto create(TrolleyDto trolleyDto) {

        trolleyDto.setId(null);
        Trolley trolleyToSave = TrolleyConverter.INSTANCE.dtoToTrolley(trolleyDto);
        Trolley savedTrolley = trolleyRepository.save(trolleyToSave);

        TrolleyDto createdDto = TrolleyConverter.INSTANCE.trolleyToDto(savedTrolley);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        TrolleyDto deletedDto = TrolleyConverter.INSTANCE.trolleyToDto(trolleyRepository.getReferenceById(id));
        trolleyRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return trolleyRepository.existsById(id);
    }
}
