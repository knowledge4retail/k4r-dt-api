package org.knowledge4retail.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.customer.service.CustomerService;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.ShoppingBasketPositionDto;
import org.knowledge4retail.api.store.exception.ShoppingBasketPositionPatchException;
import org.knowledge4retail.api.store.model.ShoppingBasketPosition;
import org.knowledge4retail.api.store.repository.ShoppingBasketRepository;
import org.knowledge4retail.api.store.service.StoreService;
import org.knowledge4retail.api.store.service.impl.ShoppingBasketServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingBasketServiceImplTest {

    @Mock
    private ShoppingBasketRepository repository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @Mock
    private StoreService storeService;

    private ShoppingBasketServiceImpl service;

    private ShoppingBasketPosition positionOne;

    private static final String PRODUCT_ID = "TST-999";


    @BeforeEach
    public void setUp() {

        service = new ShoppingBasketServiceImpl(repository, producer, customerService, productService, storeService);
        positionOne = new ShoppingBasketPosition();
        positionOne.setCustomerId(1);
        positionOne.setProductId(PRODUCT_ID);
        positionOne.setStoreId(1);
        positionOne.setQuantity(1);
        positionOne.setSellingPrice(BigDecimal.valueOf(1.11));
    }


    @Test
    public void createSendsMessageToKafka() {

        service.create(new ShoppingBasketPositionDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() throws JsonProcessingException {

        JsonPatch patch = new ObjectMapper().readValue("[{ \"op\": \"replace\", \"path\": \"/quantity\", \"value\": 2}, { \"op\": \"replace\", \"path\": \"/sellingPrice\", \"value\": 2.22}]", JsonPatch.class);
        when(repository.findById(any())).thenReturn(Optional.of(positionOne));

        service.update(any(), patch);

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteByStoreIdAndCustomerIdSendsMessageToKafka() {

        when(repository.findByStoreIdAndCustomerId(any(), any()))
                .thenReturn(new ArrayList<>() {{
                    add(positionOne);
                }});

        service.deleteByStoreIdAndCustomerId(1, 1);

        verify(producer).publishDelete(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }

    @Test
    public void applyPatchAppliesReplaceOperation() throws JsonPatchException, JsonProcessingException {
        //
        JsonPatch patch = new ObjectMapper().readValue("[ { \"op\": \"replace\", \"path\": \"/quantity\", \"value\": 3} ]", JsonPatch.class);
        //
        ShoppingBasketPosition newPosition = service.applyPatch(positionOne, patch);
        //
        assertEquals(3, newPosition.getQuantity());
    }

    @Test
    public void applyPatchAppliesMultipleReplaceOperations() throws JsonProcessingException, JsonPatchException {
        //
        JsonPatch patch = new ObjectMapper().readValue("[{ \"op\": \"replace\", \"path\": \"/quantity\", \"value\": 2}, { \"op\": \"replace\", \"path\": \"/sellingPrice\", \"value\": 2.22}]", JsonPatch.class);
        //
        ShoppingBasketPosition newPosition = service.applyPatch(positionOne, patch);
        //
        assertEquals(2, newPosition.getQuantity());
        assertEquals(BigDecimal.valueOf(2.22), newPosition.getSellingPrice());
    }



    @Test
    public void updateThrowsPatchExceptionInInvalidUpdateRequest() throws JsonProcessingException {

        JsonPatch patch = new ObjectMapper().readValue("[{ \"op\": \"replace\", \"path\": \"/INVALIDFIELD\", \"value\": 2}]", JsonPatch.class);
        when(repository.findById(any())).thenReturn(Optional.of(positionOne));

        assertThrows(ShoppingBasketPositionPatchException.class, () -> service.update(any(), patch));
    }
}