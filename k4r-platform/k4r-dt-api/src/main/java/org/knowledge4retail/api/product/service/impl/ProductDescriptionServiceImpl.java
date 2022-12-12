package org.knowledge4retail.api.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductDescriptionConverter;
import org.knowledge4retail.api.product.dto.ProductDescriptionDto;
import org.knowledge4retail.api.product.filter.ProductDescriptionFilter;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.product.repository.ProductDescriptionRepository;
import org.knowledge4retail.api.product.service.ProductDescriptionService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductDescriptionServiceImpl implements ProductDescriptionService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.productdescription}")
    private String kafkaTopic;

    private final ProductDescriptionRepository productDescriptionRepository;
    private final DefaultProducer producer;

    @Override
    public List<ProductDescriptionDto> readAll() {

        return ProductDescriptionConverter.INSTANCE.productDescriptionsToDtos(productDescriptionRepository.findAll());
    }

    @Override
    public ProductDescriptionDto read(Integer id) {

        ProductDescription productDescription = productDescriptionRepository.getReferenceById(id);
        return ProductDescriptionConverter.INSTANCE.productDescriptionToDto(productDescription);
    }

    @Override
    public ProductDescriptionDto create(ProductDescriptionDto productDescriptionDto) {

        productDescriptionDto.setId(null);
        ProductDescription DescriptionToSave = ProductDescriptionConverter.INSTANCE.dtoToProductDescription(productDescriptionDto);
        ProductDescription savedDescription = productDescriptionRepository.save(DescriptionToSave);

        ProductDescriptionDto createdDto = ProductDescriptionConverter.INSTANCE.productDescriptionToDto(savedDescription);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public ProductDescriptionDto update(Integer id, ProductDescriptionDto productDescriptionDto) {

        ProductDescriptionDto oldDto = read(id);
        productDescriptionDto.setId(id);

        ProductDescription productDescription = ProductDescriptionConverter.INSTANCE.dtoToProductDescription(productDescriptionDto);
        ProductDescriptionDto createdDto = ProductDescriptionConverter.INSTANCE.productDescriptionToDto(productDescriptionRepository.save(productDescription));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public Map<String, List<ProductDescription>> filterProductDescription(Map<Object, Object> productDescriptionContext) {

        log.info("Resolver called productService to get filtered productDescriptions");
        Set<Object> productIds = productDescriptionContext.keySet();
        Map<String, List<ProductDescription>> map = new HashMap<>();

        for (Object productObjectId : productIds) {

            String productId = productObjectId.toString();
            ProductDescriptionFilter filter = (ProductDescriptionFilter) productDescriptionContext.get(productId);
            addProductDescriptionListToMap(productId, filter, map);
        }
        return map;
    }

    public void delete(Integer id) {

        ProductDescriptionDto deletedDto = ProductDescriptionConverter.INSTANCE.productDescriptionToDto(productDescriptionRepository.getReferenceById(id));
        productDescriptionRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    public boolean exists(Integer id) {

        return productDescriptionRepository.existsById(id);
    }

    private void addProductDescriptionListToMap(String productId, ProductDescriptionFilter filter, Map<String, List<ProductDescription>> map) {

        List<ProductDescription> productDescriptions;
        if (filter == null) {

            productDescriptions = productDescriptionRepository.findByProductId(productId);
        } else {

            Specification<ProductDescription> spec = null;
            try {
                spec = getSpecification(filter, null);
            } catch (IllegalAccessException e) {
                log.error("ProductDescription could not get the specification from filter");
                e.printStackTrace();
            }

            productDescriptions = productDescriptionRepository.findAll(spec);
        }

        map.put(productId, productDescriptions);
    }
}
