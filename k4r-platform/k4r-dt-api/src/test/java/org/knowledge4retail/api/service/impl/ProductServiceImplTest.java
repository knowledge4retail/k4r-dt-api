package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.ProductDto;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.repository.ProductPropertyRepository;
import org.knowledge4retail.api.product.repository.ProductRepository;
import org.knowledge4retail.api.product.service.impl.ProductServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductPropertyRepository productPropertyRepository;
    @Mock
    DefaultProducer producer;

    ProductServiceImpl service;
    @Mock
    ProductDto productDto;
    @Mock
    Product product;


    @BeforeEach
    public void setUp() {
        service = new ProductServiceImpl(productRepository, productPropertyRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new ProductDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void createManySendsMessageToKafka() {

        List<ImportProductDto> products = new ArrayList<>();
        products.add(new ImportProductDto());
        products.add(new ImportProductDto());

        service.createMany(products);

        verify(producer, times(2)).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        service.update(productDto);

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }

}
