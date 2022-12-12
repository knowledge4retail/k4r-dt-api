package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.DespatchLineItemDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class DespatchLineItemIntegrationTest extends AbstractIntegrationTest {

    private static final String DESPATCH_LINE_ITEM_URI = "/api/v0/";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllDespatchLineItemsByDespatchLogisticUnitIdReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(DESPATCH_LINE_ITEM_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID + "/despatchlineitems")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<DespatchLineItemDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), DespatchLineItemDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void getDespatchLineItemReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(DESPATCH_LINE_ITEM_URI + "despatchlineitems/" + DESPATCH_LINE_ITEM_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        DespatchLineItemDto content = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), DespatchLineItemDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
    }

    @Test
    public void createDespatchLineItemByDespatchLogisticUnitIdReturns200Test() throws Exception {

        DespatchLineItemDto requestDto = new DespatchLineItemDto();
        requestDto.setId(1234);
        requestDto.setProductId(PRODUCT_ID);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_LINE_ITEM_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID + "/despatchlineitems").contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createDespatchLineItemByDespatchLogisticUnitIdMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_LINE_ITEM_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID + "/despatchlineitems").contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createDespatchLineItemByDespactchLogisticUnitIdWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_LINE_ITEM_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID + "/despatchlineitems").contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateDespatchLineItemReturns200Test() throws Exception {

        DespatchLineItemDto requestDto = new DespatchLineItemDto();
        requestDto.setLineItemNumber("1234");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DESPATCH_LINE_ITEM_URI + "despatchlineitems/" + DESPATCH_LINE_ITEM_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateDespatchLineItemWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DESPATCH_LINE_ITEM_URI + "despatchlineitems/" + DESPATCH_LINE_ITEM_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteDespatchLineItemReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_LINE_ITEM_URI + "despatchlineitems/" + DESPATCH_LINE_ITEM_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteDespatchLineItemMissingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_LINE_ITEM_URI + "despatchlineitems")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteDespatchLineItemNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_LINE_ITEM_URI + "despatchlineitems/" + DESPATCH_LINE_ITEM_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
