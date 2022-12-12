package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.DeliveredUnitDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class DeliveredUnitIntegrationTest extends AbstractIntegrationTest {

    private static final String DELIVERED_UNIT_URI = "/api/v0/deliveredunits";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllDeliveredUnitsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(DELIVERED_UNIT_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<DeliveredUnitDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), DeliveredUnitDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void getDeliveredUnitReturns200Test() throws Exception {

        MvcResult deliveredUnitsMvcResult = mvc.perform(MockMvcRequestBuilders.get(DELIVERED_UNIT_URI + "/" + DELIVERED_UNIT_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, deliveredUnitsMvcResult);

        DeliveredUnitDto content = mapFromJson(deliveredUnitsMvcResult.getResponse().getContentAsString(), DeliveredUnitDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
    }

    @Test
    public void createDeliveredUnitReturns200Test() throws Exception {

        DeliveredUnitDto requestDto = new DeliveredUnitDto();
        requestDto.setId(1234);
        requestDto.setAmountUnit(1);
        requestDto.setAmountItems(1);
        requestDto.setPalletId("2");
        requestDto.setProductGtinId(PRODUCT_GTIN_ID);
        requestDto.setProductUnitId(PRODUCT_UNIT_ID);
        requestDto.setWidth(1.0);
        requestDto.setHeight(1.0);
        requestDto.setDepth(1.0);
        requestDto.setTrolleyLayer(1);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DELIVERED_UNIT_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createDeliveredUnitMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DELIVERED_UNIT_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createDeliveredUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DELIVERED_UNIT_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateDeliveredUnitReturns200Test() throws Exception {

        DeliveredUnitDto requestDto = new DeliveredUnitDto();
        requestDto.setTrolleyLayer(2345);
        requestDto.setAmountUnit(1);
        requestDto.setAmountItems(1);
        requestDto.setPalletId("2");
        requestDto.setProductGtinId(PRODUCT_GTIN_ID);
        requestDto.setProductUnitId(PRODUCT_UNIT_ID);
        requestDto.setWidth(1.0);
        requestDto.setHeight(1.0);
        requestDto.setDepth(1.0);
        requestDto.setTrolleyLayer(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DELIVERED_UNIT_URI + "/" + DELIVERED_UNIT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateDeliveredUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DELIVERED_UNIT_URI + "/" + DELIVERED_UNIT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteDeliveredUnitReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DELIVERED_UNIT_URI + "/" + DELIVERED_UNIT_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteDeliveredUnitMissingIdReturn405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DELIVERED_UNIT_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteDeliveredUnitNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DELIVERED_UNIT_URI + "/" + DELIVERED_UNIT_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
