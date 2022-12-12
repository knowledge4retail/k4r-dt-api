package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.model.Product;
import org.knowledge4retail.api.product.model.ProductGroup;
import org.knowledge4retail.api.product.repository.ProductGroupRepository;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.product.service.impl.ProductGroupServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.service.StoreService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductGroupServiceImplTest {

    @Mock
    ProductGroupRepository productGroupRepository;
    @Mock
    ProductService productService;
    @Mock
    StoreService storeService;
    @Mock
    DefaultProducer producer;

    ProductGroupServiceImpl service;
    @Mock
    ProductGroup productGroup;


    @BeforeEach
    public void setUp() {
        service = new ProductGroupServiceImpl(productGroupRepository, producer, storeService);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(any());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateAddProductToGroupSendsMessageToKafka() {

        when(productGroupRepository.getReferenceById(any())).thenReturn(productGroup);

        service.updateAddProductToGroup(any(), "1");

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void updateRemoveProductFromGroupSendsMessageToKafka() {

        when(productGroupRepository.getReferenceById(any())).thenReturn(productGroup);
        Product product = new Product();
        product.setId("1");
        when(productGroup.getProducts()).thenReturn(new ArrayList<>() {{
            add(product);
        }});

        service.updateRemoveProductFromGroup(any(), "1");

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }
}
