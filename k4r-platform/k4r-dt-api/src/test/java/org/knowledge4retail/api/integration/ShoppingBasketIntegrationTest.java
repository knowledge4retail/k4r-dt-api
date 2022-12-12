package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.ShoppingBasketPositionDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.knowledge4retail.api.test.Data.*;


public class ShoppingBasketIntegrationTest extends AbstractIntegrationTest {

    private static final String URI = String.format("/api/v0/stores/%1$s/customers/%2$s/shoppingbasketpositions", STORE_ID, CUSTOMER_ID);

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @AfterEach
    public void deletePosition() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .delete(URI)).andReturn();
    }

    @Test
    public void createPositionCreatesPositionAndReturns200() throws Exception {

        ShoppingBasketPositionDto dto = ShoppingBasketPositionDto.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .sellingPrice(BigDecimal.valueOf(1.11))
                .storeId(1000)
                .customerId(1000)
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        Integer id = mapFromJson(mvcResult.getResponse().getContentAsString(), ShoppingBasketPositionDto.class).getId();
        ShoppingBasketPositionDto insertedDto = loadPosition(id);

        Assertions.assertEquals(id, insertedDto.getId());
        Assertions.assertEquals(STORE_ID, insertedDto.getStoreId());
        Assertions.assertEquals(CUSTOMER_ID, insertedDto.getCustomerId());
        Assertions.assertEquals(PRODUCT_ID, insertedDto.getProductId());
    }

    // Bei leere liste keine 404 Exception

    @Test
    public void getPositionsGetsPositionsAndReturns200() throws Exception {

        ShoppingBasketPositionDto dto = ShoppingBasketPositionDto.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .sellingPrice(BigDecimal.valueOf(1.11))
                .storeId(1000)
                .customerId(1000)
                .build();

        MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders
                .post(URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcPostResult);

        Integer insertedId = mapFromJson(mvcPostResult.getResponse().getContentAsString(), ShoppingBasketPositionDto.class).getId();

        MvcResult mvcGetResult = mvc.perform(MockMvcRequestBuilders
                .get(URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        List<ShoppingBasketPositionDto> positions =  mapListFromJson(mvcGetResult.getResponse().getContentAsString(), ShoppingBasketPositionDto.class);

        Assertions.assertTrue(positions.stream().anyMatch(x -> x.getId().equals(insertedId)));
    }

    @Test
    public void getPositions404WhenNoPosition() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(URI + "/" + PRODUCT_ID_2)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void getPositionReturnsPositionAnd200() throws Exception {

        ShoppingBasketPositionDto dto = ShoppingBasketPositionDto.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .sellingPrice(BigDecimal.valueOf(1.11))
                .storeId(1000)
                .customerId(1000)
                .build();

        MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders
                .post(URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcPostResult);

        Integer id = mapFromJson(mvcPostResult.getResponse().getContentAsString(), ShoppingBasketPositionDto.class).getId();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shoppingbasketpositions/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deletePositions404WhenNoPosition() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(URI)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deletePosition404WhenNoPosition() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/shoppingbasketpositions/" + SHOPPING_BASKET_POSITION_404)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void patchPositionUpdatesPosition() throws Exception {

        String patch = "[{ \"op\": \"replace\", \"path\": \"/quantity\", \"value\": 2}, { \"op\": \"replace\", \"path\": \"/sellingPrice\", \"value\": 2.22}]";

        ShoppingBasketPositionDto dto = ShoppingBasketPositionDto.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .sellingPrice(BigDecimal.valueOf(1.11))
                .storeId(1000)
                .customerId(1000)
                .build();

        MvcResult mvcPostResult = mvc.perform(MockMvcRequestBuilders
                .post(URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcPostResult);

        Integer insertedId = mapFromJson(mvcPostResult.getResponse().getContentAsString(), ShoppingBasketPositionDto.class).getId();

        MvcResult mvcPatchResult = mvc.perform(MockMvcRequestBuilders
                .patch("/api/v0/shoppingbasketpositions/" + insertedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(patch)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcPatchResult);

        ShoppingBasketPositionDto patchedDto = loadPosition(insertedId);

        Assertions.assertEquals(insertedId, patchedDto.getId());
        Assertions.assertEquals(CUSTOMER_ID, patchedDto.getCustomerId());
        Assertions.assertEquals(STORE_ID, patchedDto.getStoreId());
        Assertions.assertEquals(PRODUCT_ID, patchedDto.getProductId());
        Assertions.assertEquals(Integer.valueOf(2), patchedDto.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(2.22), patchedDto.getSellingPrice());
    }

    @Test
    public void patchPosition404WhenNoPosition() throws Exception {

        String patch = "[{ \"op\": \"replace\", \"path\": \"/quantity\", \"value\": 2}, { \"op\": \"replace\", \"path\": \"/sellingPrice\", \"value\": 2.22}]";

        MvcResult mvcPatchResult = mvc.perform(MockMvcRequestBuilders
                .patch("/api/v0/shoppingbasketpositions/", SHOPPING_BASKET_POSITION_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(patch)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcPatchResult);
    }

    private ShoppingBasketPositionDto loadPosition(Integer positionId) throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shoppingbasketpositions/" + positionId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        return mapFromJson(mvcResult.getResponse().getContentAsString(), ShoppingBasketPositionDto.class);
    }
}