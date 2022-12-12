package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.DespatchLogisticUnitConverter;
import org.knowledge4retail.api.store.dto.DespatchLogisticUnitDto;
import org.knowledge4retail.api.store.model.DespatchLogisticUnit;
import org.knowledge4retail.api.store.repository.DespatchLogisticUnitRepository;
import org.knowledge4retail.api.store.service.DespatchAdviceService;
import org.knowledge4retail.api.store.service.DespatchLogisticUnitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DespatchLogisticUnitServiceImpl implements DespatchLogisticUnitService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.despatchlogisticunit}")
    private String kafkaTopic;

    private final DespatchLogisticUnitRepository despatchLogisticUnitRepository;
    private final DefaultProducer producer;
    private final DespatchAdviceService despatchAdviceService;
    private final ProductService productService;

    @Override
    public List<DespatchLogisticUnitDto> readByDespatchAdviceId(Integer despatchAdviceId) {

        return DespatchLogisticUnitConverter.INSTANCE.despatchLogisticUnitToDtos(despatchLogisticUnitRepository.findByDespatchAdviceId(despatchAdviceId));
    }

    @Override
    public DespatchLogisticUnitDto read(Integer id) {

        DespatchLogisticUnit despatchLogisticUnit = despatchLogisticUnitRepository.getReferenceById(id);
        return DespatchLogisticUnitConverter.INSTANCE.despatchLogisticUnitToDto(despatchLogisticUnit);
    }

    @Override
    public DespatchLogisticUnitDto create(DespatchLogisticUnitDto despatchLogisticUnitDto) {

        despatchLogisticUnitDto.setId(null);
        DespatchLogisticUnit despatchLogisticUnitToSave = DespatchLogisticUnitConverter.INSTANCE.dtoToDespatchLogisticUnit(despatchLogisticUnitDto);
        DespatchLogisticUnit savedDespatchLogisticUnit = despatchLogisticUnitRepository.save(despatchLogisticUnitToSave);

        DespatchLogisticUnitDto createdDto = DespatchLogisticUnitConverter.INSTANCE.despatchLogisticUnitToDto(savedDespatchLogisticUnit);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public DespatchLogisticUnitDto update(Integer id, DespatchLogisticUnitDto despatchLogisticUnitDto) {

        DespatchLogisticUnitDto oldDto = read(id);
        despatchLogisticUnitDto.setId(id);

        DespatchLogisticUnit despatchLogisticUnit = DespatchLogisticUnitConverter.INSTANCE.dtoToDespatchLogisticUnit(despatchLogisticUnitDto);
        DespatchLogisticUnitDto createdDto = DespatchLogisticUnitConverter.INSTANCE.despatchLogisticUnitToDto(despatchLogisticUnitRepository.save(despatchLogisticUnit));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        DespatchLogisticUnitDto deletedDto = DespatchLogisticUnitConverter.INSTANCE.despatchLogisticUnitToDto(despatchLogisticUnitRepository.getReferenceById(id));
        despatchLogisticUnitRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return despatchLogisticUnitRepository.existsById(id);
    }

}
