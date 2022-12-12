package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.DespatchAdviceDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class DespatchAdviceIntegrationTest extends AbstractIntegrationTest {

    private static final String DESPATCH_ADVICE_URI = "/api/v0/";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllDespatchAdvicesByStoreIdReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(DESPATCH_ADVICE_URI + "stores/" + STORE_ID + "/despatchadvices")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<DespatchAdviceDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), DespatchAdviceDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        //assertNotNull(contentList.get(0).getStoreId());
    }

    @Test
    public void getDespatchAdviceReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(DESPATCH_ADVICE_URI + "despatchadvices/" + DESPATCH_ADVICE_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        DespatchAdviceDto content = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), DespatchAdviceDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
    }

    @Test
    public void createDespatchAdviceByStoreIdReturns200Test() throws Exception {

        DespatchAdviceDto requestDto = new DespatchAdviceDto();
        requestDto.setId(1234);
        requestDto.setStoreId(STORE_ID);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_ADVICE_URI + "stores/" + STORE_ID + "/despatchadvices").contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createDespatchAdviceMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_ADVICE_URI + "stores/" + STORE_ID + "/despatchadvices").contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createDespatchAdviceWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_ADVICE_URI + "stores/" + STORE_ID + "/despatchadvices").contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateDespatchAdviceReturns200Test() throws Exception {

        DespatchAdviceDto requestDto = new DespatchAdviceDto();
        requestDto.setStoreId(1000);
        requestDto.setSenderQualifier("test");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DESPATCH_ADVICE_URI + "despatchadvices/" + DESPATCH_ADVICE_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateDespatchAdviceWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DESPATCH_ADVICE_URI + "despatchadvices/" + DESPATCH_ADVICE_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteDespatchAdviceReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_ADVICE_URI + "despatchadvices/" + DESPATCH_ADVICE_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteDespatchAdviceMissingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_ADVICE_URI + "despatchadvices")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteDespatchAdviceNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_ADVICE_URI + "despatchadvices/" + DESPATCH_ADVICE_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
