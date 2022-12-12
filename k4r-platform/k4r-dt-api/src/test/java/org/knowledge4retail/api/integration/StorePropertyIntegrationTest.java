package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.StorePropertyDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class StorePropertyIntegrationTest extends AbstractIntegrationTest {

    private static final String STORE_PROPERTIES_URI = "/api/v0/stores/" + STORE_ID + "/storeproperties";
    private static final String STORE_PROPERTIES_URI_NOT_EXISTING_STORE = "/api/v0/stores/" + STORE_ID_404 + "/storeproperties";

    private static final String PROPERTY_URI = "/api/v0/stores/" + STORE_ID + "/storeproperties/" + STORE_CHARACTERISTIC_ID_PROPERTY_NEW;
    private static final String PROPERTY_URI_2 = "/api/v0/stores/" + STORE_ID + "/storeproperties/" + STORE_CHARACTERISTIC_ID_PROPERTY_DELETE;

    private static final String STORE_PROPERTY_URI_NOT_EXISTING_STORE = "/api/v0/stores/" + STORE_ID_404 +  "/storeproperties/" + STORE_CHARACTERISTIC_ID;
    private static final String STORE_PROPERTY_URI_NOT_EXISTING_CHARACTER = "/api/v0/stores/" + STORE_ID +  "/storeproperties/" + STORE_CHARACTERISTIC_ID_404;

    @BeforeEach
    @Override
    public void setUp()throws Exception{

        super.setUp();
    }

    @Test
    public void getAllStorePropertiesReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(STORE_PROPERTIES_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<StorePropertyDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), StorePropertyDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getCharacteristicId());
        Assertions.assertNotNull(contentList.get(0).getValueLow());
    }

    @Test
    public void getAllStorePropertiesForNotExistingStoreReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(STORE_PROPERTIES_URI_NOT_EXISTING_STORE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void createStorePropertyReturns200Test() throws Exception {

        StorePropertyDto requestDto = new StorePropertyDto();
        requestDto.setCharacteristicId(STORE_CHARACTERISTIC_ID);
        requestDto.setValueLow("Test value");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PROPERTY_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteStorePropertyReturns200Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PROPERTY_URI_2)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteStorePropertyForNotExistingStoreReturns404Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(STORE_PROPERTY_URI_NOT_EXISTING_STORE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteStorePropertyForNotExistingCharacteristicReturns404Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(STORE_PROPERTY_URI_NOT_EXISTING_CHARACTER)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
