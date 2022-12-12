package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.DeliveredUnitConverter;
import org.knowledge4retail.api.store.dto.DeliveredUnitDto;
import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.knowledge4retail.api.store.repository.DeliveredUnitRepository;
import org.knowledge4retail.api.store.service.DeliveredUnitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveredUnitServiceImpl implements DeliveredUnitService {


    @Value("${org.knowledge4retail.api.listener.kafka.topics.deliveredunit}")
    private String kafkaTopic;

    private final DeliveredUnitRepository deliveredUnitRepository;
    private final DefaultProducer producer;

    @Override
    public List<DeliveredUnitDto> readAll() {

        return DeliveredUnitConverter.INSTANCE.deliveredUnitsToDtos(deliveredUnitRepository.findAll());
    }

    @Override
    public DeliveredUnitDto read(Integer id) {

        DeliveredUnit deliveredUnit = deliveredUnitRepository.getReferenceById(id);
        return DeliveredUnitConverter.INSTANCE.deliveredUnitToDto(deliveredUnit);
    }

    @Override
    public DeliveredUnitDto create(DeliveredUnitDto deliveredUnitDto) {

        deliveredUnitDto.setId(null);
        DeliveredUnit deliveredUnitToSave = DeliveredUnitConverter.INSTANCE.dtoToDeliveredUnit(deliveredUnitDto);
        DeliveredUnit savedDeliveredUnit = deliveredUnitRepository.save(deliveredUnitToSave);

        DeliveredUnitDto createdDto = DeliveredUnitConverter.INSTANCE.deliveredUnitToDto(savedDeliveredUnit);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public DeliveredUnitDto update(Integer id, DeliveredUnitDto deliveredUnitDto) {

        DeliveredUnitDto oldDto = read(id);
        deliveredUnitDto.setId(id);

        DeliveredUnit deliveredUnit = DeliveredUnitConverter.INSTANCE.dtoToDeliveredUnit(deliveredUnitDto);
        DeliveredUnitDto createdDto = DeliveredUnitConverter.INSTANCE.deliveredUnitToDto(deliveredUnitRepository.save(deliveredUnit));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        DeliveredUnitDto deletedDto = DeliveredUnitConverter.INSTANCE.deliveredUnitToDto(deliveredUnitRepository.getReferenceById(id));
        deliveredUnitRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return deliveredUnitRepository.existsById(id);
    }

}
