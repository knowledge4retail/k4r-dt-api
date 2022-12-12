package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowledge4retail.api.test.Data.*;

public class ShelfLayerIntegrationTest extends AbstractIntegrationTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void createShelfLayerCreatesShelfLayerAndReturns200() throws Exception {

        final String layerType = "testLayer";
        ShelfLayerDto dto = ShelfLayerDto.builder().width(1.0).height(1.0).depth(1.0).lengthUnitId(1000).type(layerType).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/shelves/%1$s/shelflayers", SHELF_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ShelfLayerDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfLayerDto.class);

        assertEquals(layerType, returnedDto.getType());
        assertNotNull(returnedDto.getId());
    }

    @Test
    public void createShelfLayerUsesShelfIdFromPath() throws Exception {

        ShelfLayerDto dto = ShelfLayerDto.builder().width(1.0).height(1.0).depth(1.0).lengthUnitId(1000).shelfId(SHELF_ID_404).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/shelves/%1$s/shelflayers", SHELF_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ShelfLayerDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfLayerDto.class);

        assertEquals(SHELF_ID, returnedDto.getShelfId());
    }

    @Test
    public void createShelfLayerReturns404WhenShelfNotFound() throws Exception {

        ShelfLayerDto dto = ShelfLayerDto.builder().width(1.0).height(1.0).depth(1.0).lengthUnitId(1000).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/shelves/%1$s/shelflayers", SHELF_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void createShelfLayerReturns400OnMalformedRequestBody() throws Exception {

        String dtoString = "{ \"width\": \"thisShouldBeAnumber\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/shelves/%1$s/shelflayers", SHELF_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dtoString)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void retrieveShelfLayersReturnsShelfLayersAnd200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/shelves/%1$s/shelflayers", SHELF_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<ShelfLayerDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), ShelfLayerDto.class);

        assertTrue(returnedDtos.size() >= 1);
    }

    @Test
    public void retrieveShelfLayersReturns404WhenShelfNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/shelves/%1$s/shelflayers", SHELF_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void retrieveShelfLayerReturnsShelfLayerAnd200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelflayers/" + SHELF_LAYER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ShelfLayerDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ShelfLayerDto.class);

        assertEquals(SHELF_LAYER_ID, returnedDto.getId());
    }

    @Test
    public void retrieveShelfLayerReturns404WhenShelfLayerNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelflayers/" + SHELF_LAYER_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void deleteShelfLayerDeletesShelfLayerAndReturns200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/shelflayers/" + SHELF_LAYER_ID_DELETE_2)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/shelflayers/" + SHELF_LAYER_ID_DELETE_2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result2);
    }

    @Test
    public void deleteShelfLayerReturns404WhenShelfLayerNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/shelflayers/" + SHELF_LAYER_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }
}
