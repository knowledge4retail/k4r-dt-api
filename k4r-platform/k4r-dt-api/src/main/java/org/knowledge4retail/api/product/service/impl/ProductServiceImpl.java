package org.knowledge4retail.api.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.converter.ProductConverter;
import org.knowledge4retail.api.product.converter.ProductPropertyConverter;
import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.ProductDto;
import org.knowledge4retail.api.product.dto.ProductPropertyDto;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.model.ProductProperty;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.product.repository.ProductRepository;
import org.knowledge4retail.api.product.repository.ProductPropertyRepository;
import org.knowledge4retail.api.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.product}")
    private String kafkaTopic;

    private final ProductRepository productRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final DefaultProducer defaultProducer;

    @Override
    public List<ProductDto> readAll() {
        return ProductConverter.INSTANCE.productsToDtos(productRepository.findAll());
    }

    @Override
    public ProductDto read(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(ProductConverter.INSTANCE::productToDto).orElse(null);
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto productDto) {
        Optional<Product> oldProduct = productRepository.findById(productDto.getId());
        if (oldProduct.isPresent()){
            Product newProduct = ProductConverter.INSTANCE.dtoToProduct(productDto);
            productRepository.save(newProduct);
            ProductDto newProductDto = ProductConverter.INSTANCE.productToDto(newProduct);
            defaultProducer.publishUpdate(kafkaTopic,
                    newProductDto,
                    ProductConverter.INSTANCE.productToDto(oldProduct.get()));
            return newProductDto;
        }
        return null;
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        ProductDto createdProduct = ProductConverter.INSTANCE.productToDto(productRepository.save(ProductConverter.INSTANCE.dtoToProduct(productDto)));
        publishCreationKafkaMessage(createdProduct);
        return productDto;
    }

    @Override
    public void delete(String id) {
        Optional<Product> productOp = productRepository.findById(id);
        if (productOp.isPresent()){
            defaultProducer.publishDelete(kafkaTopic, ProductConverter.INSTANCE.productToDto(productOp.get()));
            productRepository.deleteById(id);
        }

    }

    @Override
    @Transactional
    //TODO: this implementation is a workaround - find a better solution which provides batch insert for all products and their properties
    public List<ImportProductDto> createMany(List<ImportProductDto> productDtos) {
        List<ImportProductDto> savedImportProducts = new ArrayList<>();
        for (ImportProductDto productDto : productDtos){
            ImportProductDto savedProduct = saveProductWithProperties(productDto);
            savedImportProducts.add(savedProduct);
            publishCreationKafkaMessage(savedProduct);
        }
        return savedImportProducts;
    }

    @Override
    public boolean exists(String id) {
        return productRepository.existsById(id);
    }


    private void publishCreationKafkaMessage(ProductDto createdProduct) {
        defaultProducer.publishCreate(kafkaTopic, createdProduct);
    }

    private ImportProductDto saveProductWithProperties(ImportProductDto productDto) {
        Product productEntity = ProductConverter.INSTANCE.importDtoToProduct(productDto);
        Product savedProduct = productRepository.save(productEntity);
        List<ProductPropertyDto> productProperties = productDto.getProperties();
        ImportProductDto savedProductDto = ProductConverter.INSTANCE.productToImportDto(savedProduct);
        if (productProperties != null){
            savedProductDto.setProperties(savePropertiesForProduct(savedProduct, productProperties));
        }
        return savedProductDto;
    }

    private List<ProductPropertyDto> savePropertiesForProduct(Product savedProduct, List<ProductPropertyDto> productProperties) {
        productProperties.forEach(property -> property.setProductId(savedProduct.getId()));
        List<ProductProperty> savedProperties = productPropertyRepository.saveAll(ProductPropertyConverter.INSTANCE.dtosToProperties(productProperties));
        return ProductPropertyConverter.INSTANCE.propertiesToDtos(savedProperties);
    }

}
