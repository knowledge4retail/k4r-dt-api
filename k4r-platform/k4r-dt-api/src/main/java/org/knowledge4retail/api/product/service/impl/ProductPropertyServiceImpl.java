package org.knowledge4retail.api.product.service.impl;

import org.knowledge4retail.api.product.converter.ProductPropertyConverter;
import org.knowledge4retail.api.product.dto.ProductPropertyDto;
import org.knowledge4retail.api.product.model.ProductProperty;
import org.knowledge4retail.api.product.repository.ProductPropertyRepository;
import org.knowledge4retail.api.product.service.ProductPropertyService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductPropertyServiceImpl implements ProductPropertyService {

    private final ProductPropertyRepository productPropertyRepository;
    private final DefaultProducer producer;
    @Value("${org.knowledge4retail.api.listener.kafka.topics.productproperty}")
    private String kafkaTopic;

    public ProductPropertyServiceImpl(ProductPropertyRepository productPropertyRepository, DefaultProducer producer){

        this.productPropertyRepository = productPropertyRepository;
        this.producer = producer;
    }

    public List<ProductPropertyDto> readByStoreIdAndProductId(Integer storeId, String productId){

        return ProductPropertyConverter.INSTANCE.propertiesToDtos(productPropertyRepository.getAllByProductIdAndStoreId(storeId, productId));
    }

    public List<ProductPropertyDto> readByStoreIdNullAndProductId(String productId){

        return ProductPropertyConverter.INSTANCE.propertiesToDtos(productPropertyRepository.getAllByProductIdAndStoreIdNull(productId));
    }

    public ProductPropertyDto readByStoreIdAndProductIdAndCharacteristicId(Integer storeId, String productId, Integer characteristicId){

        if (storeId == null) {

            return ProductPropertyConverter.INSTANCE.propertyToDto(productPropertyRepository.getOneByProductIdAndCharacteristicIdAndStoreIdNull(productId, characteristicId));
        }
        return ProductPropertyConverter.INSTANCE.propertyToDto(productPropertyRepository.getOneByProductIdAndCharacteristicIdAndStoreId(productId, characteristicId, storeId));
    }

    @Override
    public ProductPropertyDto create(ProductPropertyDto productPropertyDto) {

        productPropertyDto.setId(null);
        ProductProperty propertyToSave = ProductPropertyConverter.INSTANCE.dtoToProperty(productPropertyDto);
        ProductProperty savedProperty = productPropertyRepository.save(propertyToSave);

        ProductPropertyDto createdDto = ProductPropertyConverter.INSTANCE.propertyToDto(savedProperty);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    @Transactional
    public void delete(Integer storeId, String productId, Integer characteristicId) {

        Optional<ProductProperty> propertyOpt = productPropertyRepository.getProperty(storeId, productId, characteristicId);
        if (propertyOpt.isPresent()){

            ProductPropertyDto deletedDto = ProductPropertyConverter.INSTANCE.propertyToDto(propertyOpt.get());
            productPropertyRepository.deleteProperty(storeId, productId, characteristicId);
            producer.publishDelete(kafkaTopic, deletedDto);
        }
    }

    @Override
    @Transactional
    public void deleteByStoreIdNullAndProductIdAndCharacteristicId(String productId, Integer characteristicId) {

        Optional<ProductProperty> propertyOpt = productPropertyRepository.getPropertyWithStoreIdNull(productId, characteristicId);
        if (propertyOpt.isPresent()){

            ProductPropertyDto deletedDto = ProductPropertyConverter.INSTANCE.propertyToDto(propertyOpt.get());
            productPropertyRepository.deletePropertyWithStoreIdNull(productId, characteristicId);
            producer.publishDelete(kafkaTopic, deletedDto);
        }
    }

    public boolean existsByStoreIdNullAndProductIdAndCharacteristicId(String productId, Integer characteristicId){

        return productPropertyRepository.existsByProductIdAndCharacteristicIdAndStoreIdNull(productId, characteristicId);
    }

    public boolean exists(Integer storeId, String productId, Integer characteristicId){

        return productPropertyRepository.existsByProductIdAndCharacteristicIdAndStoreId(storeId, productId, characteristicId);
    }

    @Override
    public ProductPropertyDto update(Integer storeId, String productId, Integer characteristicId,  ProductPropertyDto productPropertyDto) {

        ProductPropertyDto oldDto = readByStoreIdAndProductIdAndCharacteristicId(storeId, productId, characteristicId);
        productPropertyDto.setId(oldDto.getId());

        ProductProperty productProperty = ProductPropertyConverter.INSTANCE.dtoToProperty(productPropertyDto);
        ProductPropertyDto createdDto = ProductPropertyConverter.INSTANCE.propertyToDto(productPropertyRepository.save(productProperty));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

}
