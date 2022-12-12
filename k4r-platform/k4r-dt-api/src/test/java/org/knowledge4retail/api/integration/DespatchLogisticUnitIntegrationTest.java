package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.DespatchLogisticUnitDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class DespatchLogisticUnitIntegrationTest extends AbstractIntegrationTest {

    private static final String DESPATCH_LOGISTIC_UNIT_URI = "/api/v0/";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllDespatchLogisticUnitsByDespatchAdviceIdReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(DESPATCH_LOGISTIC_UNIT_URI + "despatchadvices/" + DESPATCH_ADVICE_ID + "/despatchlogisticunits")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<DespatchLogisticUnitDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), DespatchLogisticUnitDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void getDespatchLogisticUnitReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(DESPATCH_LOGISTIC_UNIT_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        DespatchLogisticUnitDto content = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), DespatchLogisticUnitDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
    }

    @Test
    public void createDespatchLogisticUnitByDespatchAdviceIdReturns200Test() throws Exception {

        DespatchLogisticUnitDto requestDto = new DespatchLogisticUnitDto();
        requestDto.setId(1234);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_LOGISTIC_UNIT_URI + "despatchadvices/" + DESPATCH_ADVICE_ID + "/despatchlogisticunits").contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createDespatchLogisticUnitByDespatchAdviceIdMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_LOGISTIC_UNIT_URI + "despatchadvices/" + DESPATCH_ADVICE_ID + "/despatchlogisticunits").contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createDespatchLogisticByDespatchAdviceIdUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DESPATCH_LOGISTIC_UNIT_URI + "despatchadvices/" + DESPATCH_ADVICE_ID + "/despatchlogisticunits").contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateDespatchLogisticUnitReturns200Test() throws Exception {

        DespatchLogisticUnitDto requestDto = new DespatchLogisticUnitDto();
        requestDto.setLogisticUnitId("1234");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DESPATCH_LOGISTIC_UNIT_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateDespatchLogisticUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(DESPATCH_LOGISTIC_UNIT_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteDespatchLogisticUnitReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_LOGISTIC_UNIT_URI + "despatchlogisticunits/" + DESPATCH_LOGISTIC_UNIT_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteDespatchLogisticUnitMissingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_LOGISTIC_UNIT_URI + "despatchlogisticunits")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteDespatchLogisticUnitNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DESPATCH_LOGISTIC_UNIT_URI + "despatchlogisticunits" + "/" + DESPATCH_LOGISTIC_UNIT_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
