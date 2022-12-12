package org.knowledge4retail.api.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.UnitConverter;
import org.knowledge4retail.api.product.dto.UnitDto;
import org.knowledge4retail.api.product.model.Unit;
import org.knowledge4retail.api.product.repository.UnitRepository;
import org.knowledge4retail.api.product.service.UnitService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.unit}")
    private String kafkaTopic;

    public UnitServiceImpl(UnitRepository unitRepository, DefaultProducer producer) {

        this.unitRepository = unitRepository;
        this.producer = producer;
    }

    @Override
    public List<UnitDto> readAll() {

        return UnitConverter.INSTANCE.unitsToDtos(unitRepository.findAll());
    }

    @Override
    public UnitDto create(UnitDto unitDto) {

        unitDto.setId(null);
        Unit unitToSave = UnitConverter.INSTANCE.dtoToUnit(unitDto);
        Unit savedUnit = unitRepository.save(unitToSave);

        UnitDto createdDto = UnitConverter.INSTANCE.unitToDto(savedUnit);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    public void delete(Integer id) {

        UnitDto deletedDto = UnitConverter.INSTANCE.unitToDto(unitRepository.getReferenceById(id));
        unitRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    public boolean exists(Integer id) {

        return unitRepository.existsById(id);
    }
}
