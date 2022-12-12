package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.UnitDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class UnitIntegrationTest extends AbstractIntegrationTest {

    private static final String UNIT_URI = "/api/v0/units";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllUnitsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(UNIT_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<UnitDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), UnitDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void createUnitReturns200Test() throws Exception {

        UnitDto requestDto = new UnitDto();
        requestDto.setId(1234);
        requestDto.setId(UNIT_ID);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(UNIT_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createUnitMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(UNIT_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(UNIT_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteUnitReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(UNIT_URI + "/" + UNIT_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteUnitMissingIdReturns405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(UNIT_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteUnitNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(UNIT_URI + "/" + UNIT_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
