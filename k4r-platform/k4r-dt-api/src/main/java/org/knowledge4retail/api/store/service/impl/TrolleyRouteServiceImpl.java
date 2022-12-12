package org.knowledge4retail.api.store.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.TrolleyRouteConverter;
import org.knowledge4retail.api.store.dto.TrolleyRouteDto;
import org.knowledge4retail.api.store.model.TrolleyRoute;
import org.knowledge4retail.api.store.repository.TrolleyRouteRepository;
import org.knowledge4retail.api.store.service.TrolleyRouteService;
import org.knowledge4retail.api.store.service.TrolleyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrolleyRouteServiceImpl implements TrolleyRouteService {

    private final TrolleyRouteRepository trolleyRouteRepository;
    private final TrolleyService trolleyService;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.trolleyroute}")
    private String kafkaTopic;

    public TrolleyRouteServiceImpl(TrolleyRouteRepository trolleyRouteRepository, TrolleyService trolleyService, DefaultProducer producer) {

        this.trolleyRouteRepository = trolleyRouteRepository;
        this.trolleyService = trolleyService;
        this.producer = producer;
    }

    @Override
    public List<TrolleyRouteDto> readByTrolleyId(Integer storeId) {

        return TrolleyRouteConverter.INSTANCE.trolleyRoutesToDtos(trolleyRouteRepository.findByTrolleyId(storeId));
    }

    @Override
    public TrolleyRouteDto create(TrolleyRouteDto trolleyRouteDto) {

        trolleyRouteDto.setId(null);
        TrolleyRoute trolleyRouteToSave = TrolleyRouteConverter.INSTANCE.dtoToTrolleyRoute(trolleyRouteDto);
        TrolleyRoute savedTrolleyRoute = trolleyRouteRepository.save(trolleyRouteToSave);

        TrolleyRouteDto createdDto = TrolleyRouteConverter.INSTANCE.trolleyRouteToDto(savedTrolleyRoute);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        TrolleyRouteDto deletedDto = TrolleyRouteConverter.INSTANCE.trolleyRouteToDto(trolleyRouteRepository.getReferenceById(id));
        trolleyRouteRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return trolleyRouteRepository.existsById(id);
    }
}
