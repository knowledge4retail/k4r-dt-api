package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductPropertyConverter;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.SalesConverter;
import org.knowledge4retail.api.store.dto.SalesDto;
import org.knowledge4retail.api.store.model.Sales;
import org.knowledge4retail.api.store.repository.SalesRepository;
import org.knowledge4retail.api.store.service.SalesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.sales}")
    private String kafkaTopic;

    @Override
    public List<SalesDto> readAll() {

        return SalesConverter.INSTANCE.salesToDtos(salesRepository.findAll());
    }

    @Override
    public List<SalesDto> read(Integer storeId, String gtin) {

        List<Sales> sales = salesRepository.findByStoreIdAndGtin(storeId, gtin);
        return SalesConverter.INSTANCE.salesToDtos(sales);
    }

    @Override
    @Transactional
    public SalesDto create(SalesDto salesDto) {

        Sales salesToSave = SalesConverter.INSTANCE.dtoToSales(salesDto);
        Sales savedSales = salesRepository.save(salesToSave);

        SalesDto createdDto = SalesConverter.INSTANCE.salesToDto(savedSales);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public List<SalesDto> createMany(List<SalesDto> salesDtos) {

        List<Sales> savedSales = salesRepository.saveAll(SalesConverter.INSTANCE.dtosToSales(salesDtos));
        List<SalesDto> createdDtos = SalesConverter.INSTANCE.salesToDtos(savedSales);
        for (SalesDto createdDto: createdDtos){
            producer.publishCreate(kafkaTopic, createdDto);
        }
        return createdDtos;
    }

    @Override
    public boolean exists(Integer storeId, String gtin) {

        return salesRepository.existsByStoreIdAndGtin(storeId, gtin);
    }
}
