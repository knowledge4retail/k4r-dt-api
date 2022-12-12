package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.ProductPropertyDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class ProductPropertyIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCT_PROPERTIES_URI = "/api/v0/products/" + PRODUCT_ID + "/productproperties";
    private static final String PRODUCT_PROPERTIES_URI_NOT_EXISTING_STORE = "/api/v0/products/" + PRODUCT_ID + "/productproperties";
    private static final String PRODUCT_PROPERTIES_URI_NOT_EXISTING_PRODUCT = "/api/v0/products/" + PRODUCT_ID_404 + "/productproperties";

    private static final String PROPERTY_URI = "/api/v0/products/" + PRODUCT_ID + "/productproperties/" + PRODUCT_CHARACTERISTIC_ID_PROPERTY_NEW;
    private static final String PROPERTY_URI_2 = "/api/v0/products/" + PRODUCT_ID + "/productproperties/" + PRODUCT_CHARACTERISTIC_ID_PROPERTY_DELETE;

    private static final String PRODUCT_PROPERTY_URI_NOT_EXISTING_STORE = "/api/v0/products/" + PRODUCT_ID + "/productproperties/" + PRODUCT_CHARACTERISTIC_ID + "?storeId=" + STORE_ID_404;
    private static final String PRODUCT_PROPERTY_URI_NOT_EXISTING_PRODUCT = "/api/v0/products/" + PRODUCT_ID_404 + "/productproperties/" + PRODUCT_CHARACTERISTIC_ID;
    private static final String PRODUCT_PROPERTY_URI_NOT_EXISTING_CHARACTER = "/api/v0/products/" + PRODUCT_ID + "/productproperties/" + PRODUCT_CHARACTERISTIC_ID_404;

    @BeforeEach
    @Override
    public void setUp()throws Exception{

        super.setUp();
    }

    @Test
    public void getAllProductPropertiesReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_PROPERTIES_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<ProductPropertyDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), ProductPropertyDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getCharacteristicId());
        Assertions.assertNotNull(contentList.get(0).getProductId());
        Assertions.assertNotNull(contentList.get(0).getValueLow());
    }

    @Test
    public void getAllProductPropertiesForNotExistingStoreReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_PROPERTIES_URI_NOT_EXISTING_STORE).param("storeId", STORE_ID_404.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void getAllProductPropertiesForNotExistingProductReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_PROPERTIES_URI_NOT_EXISTING_PRODUCT).param("storeId", STORE_ID.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void createProductPropertyReturns200Test() throws Exception {

        ProductPropertyDto requestDto = new ProductPropertyDto();
        requestDto.setProductId(PRODUCT_ID);
        requestDto.setCharacteristicId(PRODUCT_CHARACTERISTIC_ID);
        requestDto.setValueLow("Test value");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PROPERTY_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteProductPropertyReturns200Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PROPERTY_URI_2).param("storeId", STORE_ID.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteProductPropertyForNotExistingStoreReturns404Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_PROPERTY_URI_NOT_EXISTING_STORE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteProductPropertyForNotExistingProductReturns404Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_PROPERTY_URI_NOT_EXISTING_PRODUCT)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteProductPropertyForNotExistingCharacteristicReturns404Test() throws Exception{

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_PROPERTY_URI_NOT_EXISTING_CHARACTER)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
