package org.knowledge4retail.api.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.StoreObjectDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowledge4retail.api.test.Data.*;

@Slf4j
public class StoreObjectIntegrationTest extends AbstractIntegrationTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void createStoreObjectCreatesStoreObjectAndReturns200() throws Exception {

        String type = "testtype";
        StoreObjectDto dto = StoreObjectDto.builder()
                .width(1).height(1).depth(1).lengthUnitId(1000).type(type)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        StoreObjectDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), StoreObjectDto.class);

        assertEquals(type, returnedDto.getType());
        assertEquals(STORE_ID, returnedDto.getStoreId());
        assertNotNull(returnedDto.getId());

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/storeobjects/" + returnedDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void createStoreObjectUsesStoreIdFromPath() throws Exception {

        StoreObjectDto dto = StoreObjectDto.builder()
                .width(1).height(1).depth(1).lengthUnitId(1000).type("testtype")
                .storeId(STORE_ID_404)
                .build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        StoreObjectDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), StoreObjectDto.class);

        assertEquals(STORE_ID, returnedDto.getStoreId());

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/storeobjects/" + returnedDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
    }

    @Test
    public void createStoreObjectReturns404WhenStoreNotFound() throws Exception {

        StoreObjectDto dto = StoreObjectDto.builder().width(1).height(1).depth(1).lengthUnitId(1000).type("testtype").build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void createStoreObjectReturns400OnMalformedRequestBody() throws Exception {

        String dtoString = "{ \"width\": \"thisShouldBeAnumber\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(dtoString)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void retrieveStoreObjectsReturnsStoreObjectsAnd200WhenNoTypeIsGiven() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<StoreObjectDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), StoreObjectDto.class);

        assertTrue(returnedDtos.size() >= 1);
    }

    @Test
    public void retrieveStoreObjectsFiltersByTypeAndReturns200() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/storeobjects?type=cashzone", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<StoreObjectDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), StoreObjectDto.class);

        assertEquals(1, returnedDtos.size());
    }

    @Test
    public void retrieveStoreObjectsReturns404WhenStoreNotFound() throws Exception{

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void deleteStoreObjectDeletesStoreObjectAndReturns200() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/storeobjects/" + STORE_OBJECT_DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/storeobjects", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        List<StoreObjectDto> returnedDtos = mapListFromJson(result2.getResponse().getContentAsString(), StoreObjectDto.class);

        assertEquals(2, returnedDtos.size());
    }

    @Test
    public void deleteStoreObjectReturns404WhenStoreObjectNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/storeobjects/" + STORE_OBJECT_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }
}
