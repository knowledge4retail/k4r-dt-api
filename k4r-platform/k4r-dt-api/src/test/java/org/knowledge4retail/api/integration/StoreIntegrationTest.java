package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.StoreAggregateDto;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;


public class StoreIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URI = "/api/v0/stores";

    @BeforeEach
    @Override
    public void setUp() throws Exception{ super.setUp(); }

    @Test
    public void getAllStoresReturnsStoresInDbAnd200() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(BASE_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<StoreDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), StoreDto.class);

        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getStoreName());
    }

    @Test
    public void getStoreAggregatesReturnsCountedAggregates() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(BASE_URI + "/aggregates")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<StoreAggregateDto> content = mapListFromJson(mvcResult.getResponse().getContentAsString(), StoreAggregateDto.class);

        Assertions.assertNotNull(content);
        Assertions.assertTrue(content.size() > 0);
        StoreAggregateDto store1000 = content.stream().filter(it -> it.getId().equals(1000)).findFirst().get();
        Assertions.assertNull(store1000.getLatitude());
        Assertions.assertTrue(store1000.getShelfCount() > 0);
        Assertions.assertTrue(store1000.getShelfLayerCount() > 0);
        Assertions.assertTrue(store1000.getBarcodeCount() > 0);
        Assertions.assertTrue(store1000.getProductCount() > 0);
    }

    @Test
    public void getStoreByIdReturnsStore() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                        .get(BASE_URI + "/" + STORE_ID)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        StoreDto content = mapFromJson(mvcResult.getResponse().getContentAsString(), StoreDto.class);

        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
        Assertions.assertNotNull(content.getStoreName());
    }

    @Test
    public void createStoreReturns200() throws Exception {

        StoreDto dto = new StoreDto();
        dto.setStoreName("Test Store");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createStoreMissingBodyReturns400() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createStoreWrongBodyReturns400() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("testing")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createStoreMissingNameReturns400() throws Exception {

        StoreDto dto = new StoreDto();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateStoreReturns200() throws Exception {

        StoreDto dto = new StoreDto();
        dto.setStoreName("new name");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(BASE_URI + "/" + STORE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateStoreWrongBodyReturns400() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(BASE_URI + "/" + STORE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateStoreNotExistingIdReturns404() throws Exception {

        StoreDto dto = new StoreDto();
        dto.setStoreName("new name");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URI + "/" + STORE_ID_404)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteStoreReturns200() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URI + "/" + STORE_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteStoreMissingIdReturns405() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteStoreNotExistingIdReturns404() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URI + "/" + STORE_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
