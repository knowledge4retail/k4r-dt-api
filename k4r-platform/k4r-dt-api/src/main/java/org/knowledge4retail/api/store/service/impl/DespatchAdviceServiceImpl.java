package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.DespatchAdviceConverter;
import org.knowledge4retail.api.store.dto.DespatchAdviceDto;
import org.knowledge4retail.api.store.model.DespatchAdvice;
import org.knowledge4retail.api.store.repository.DespatchAdviceRepository;
import org.knowledge4retail.api.store.service.DespatchAdviceService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DespatchAdviceServiceImpl implements DespatchAdviceService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.despatchadvice}")
    private String kafkaTopic;

    private final DespatchAdviceRepository despatchAdviceRepository;
    private final DefaultProducer producer;
    private final StoreService storeService;

    @Override
    public List<DespatchAdviceDto> readByStoreId(Integer storeId) {

        return DespatchAdviceConverter.INSTANCE.despatchAdvicesToDtos(despatchAdviceRepository.findByStoreId(storeId));
    }

    @Override
    public DespatchAdviceDto read(Integer id) {

        DespatchAdvice despatchAdvice = despatchAdviceRepository.getReferenceById(id);
        return DespatchAdviceConverter.INSTANCE.despatchAdviceToDto(despatchAdvice);
    }

    @Override
    public DespatchAdviceDto create(DespatchAdviceDto despatchAdviceDto) {

        despatchAdviceDto.setId(null);
        DespatchAdvice despatchAdviceToSave = DespatchAdviceConverter.INSTANCE.dtoToDespatchAdvice(despatchAdviceDto);
        DespatchAdvice savedDespatchAdvice = despatchAdviceRepository.save(despatchAdviceToSave);

        DespatchAdviceDto createdDto = DespatchAdviceConverter.INSTANCE.despatchAdviceToDto(savedDespatchAdvice);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public DespatchAdviceDto update(Integer id, DespatchAdviceDto despatchAdviceDto) {

        DespatchAdviceDto oldDto = read(id);
        despatchAdviceDto.setId(id);

        DespatchAdvice despatchAdvice = DespatchAdviceConverter.INSTANCE.dtoToDespatchAdvice(despatchAdviceDto);
        DespatchAdviceDto createdDto = DespatchAdviceConverter.INSTANCE.despatchAdviceToDto(despatchAdviceRepository.save(despatchAdvice));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        DespatchAdviceDto deletedDto = DespatchAdviceConverter.INSTANCE.despatchAdviceToDto(despatchAdviceRepository.getReferenceById(id));
        despatchAdviceRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return despatchAdviceRepository.existsById(id);
    }

}
