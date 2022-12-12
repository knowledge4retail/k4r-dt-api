package org.knowledge4retail.api.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowledge4retail.api.test.Data.*;

@Slf4j
public class ShelfIntegrationTest extends AbstractIntegrationTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void createShelfCreatesShelfAndReturns200() throws Exception {

        String cadPlanId = "my-Shelf";
        ShelfDto dto = ShelfDto.builder()
                .width(1.0).height(1.0).depth(1.0).lengthUnitId(1000).cadPlanId(cadPlanId)
                .productGroupId(PRODUCT_GROUP_ID)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/shelves", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ShelfDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfDto.class);

        assertEquals(cadPlanId, returnedDto.getCadPlanId());
        assertEquals(STORE_ID, returnedDto.getStoreId());
        assertNotNull(returnedDto.getId());
    }

    @Test
    public void createShelfUsesStoreIdFromPath() throws Exception {

        ShelfDto dto = ShelfDto.builder()
                .width(1.0).height(1.0).depth(1.0).lengthUnitId(1000)
                .storeId(STORE_ID_404)
                .productGroupId(PRODUCT_GROUP_ID)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/shelves", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ShelfDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfDto.class);

        assertEquals(STORE_ID, returnedDto.getStoreId());
    }

    @Test
    public void createShelfReturns404WhenStoreNotFound() throws Exception {

        ShelfDto dto = ShelfDto.builder().width(1.0).height(1.0).depth(1.0).lengthUnitId(1000).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/shelves", STORE_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void createShelfReturns404WhenProductGroupNotFound() throws Exception {

        ShelfDto dto = ShelfDto.builder()
                .width(1.0).height(1.0).depth(1.0).lengthUnitId(1000)
                .productGroupId(PRODUCT_GROUP_ID_404)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/shelves", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void createShelfReturns200WhenNoProductGroupWasGiven() throws Exception {

        ShelfDto dto = ShelfDto.builder()
                .width(1.0).height(1.0).depth(1.0).lengthUnitId(1000)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/shelves", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);
    }

    @Test
    public void createShelfReturns400OnMalformedRequestBody() throws Exception {

        String dtoString = "{ \"width\": \"thisShouldBeAnumber\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/shelves", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dtoString)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void retrieveShelvesReturnsShelvesAnd200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/shelves", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<ShelfDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), ShelfDto.class);

        assertTrue(returnedDtos.size() >= 1);
    }

    @Test
    public void retrieveShelvesReturns404WhenStoreNotFound() throws Exception{

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/shelves", STORE_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void retrieveShelfReturnsShelfAnd200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelves/" + SHELF_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ShelfDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfDto.class);

        assertEquals(SHELF_ID, returnedDto.getId());
    }

    @Test
    public void retrieveShelfReturns404WhenShelfNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelves/" + SHELF_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void updateShelfUpdatesShelfAndReturns200() throws Exception {

        String dtoString = "{ \"width\": 1, \"height\": 1, \"depth\": 1, \"lengthUnitId\": 1000, \"storeId\": 1000, \"cadPlanId\": \"newCadPlanId\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v0/shelves/" + SHELF_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dtoString)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ShelfDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfDto.class);

        assertEquals("newCadPlanId", returnedDto.getCadPlanId());
    }

    @Test
    public void updateShelfUsesShelfIdFromPath() throws Exception {

        ShelfDto newDto = ShelfDto.builder().width(WIDTH).height(HEIGHT).depth(DEPTH).lengthUnitId(UNIT_ID).id(SHELF_ID_404).storeId(STORE_ID).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v0/shelves/" + SHELF_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newDto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ShelfDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfDto.class);

        assertEquals(SHELF_ID, returnedDto.getId());
    }

    @Test
    public void updateShelfReturns404WhenShelfNotFound() throws Exception {

        ShelfDto newDto = ShelfDto.builder().width(1.0).height(1.0).depth(1.0).lengthUnitId(1000).cadPlanId("newCadPlanId").build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v0/shelves/" + SHELF_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newDto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void updateShelfReturns404WhenProductGroupNotFound() throws Exception {

        String dtoString = "{ \"width\": 1, \"height\": 1, \"depth\": 1, \"lengthUnitId\": 1000, \"cadPlanId\": \"newCadPlanId\", \"productGroupId\": \"" + PRODUCT_GROUP_ID_404 + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v0/shelves/" + SHELF_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dtoString)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void updateShelfReturns400OnMalformedRequestBody() throws Exception {

        String dtoString = "{ \"width\": \"thisShouldBeAnumber\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v0/shelves/" + SHELF_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dtoString)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void deleteShelfDeletesShelfAndReturns200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/shelves/" + SHELF_ID_DELETE_2)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelves/" + SHELF_ID_DELETE_2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result2);
    }

    @Test
    public void deleteShelfAlsoDeletesShelfLayers() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/shelves/" + SHELF_ID_DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelflayers/" + SHELF_LAYER_ID_DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result2);
    }

    public void deleteShelfReturns404WhenShelfNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/shelves/" + SHELF_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }
}
