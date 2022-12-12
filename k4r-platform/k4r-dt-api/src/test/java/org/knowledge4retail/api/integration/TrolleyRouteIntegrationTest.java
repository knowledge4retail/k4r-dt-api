package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.TrolleyRouteDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class TrolleyRouteIntegrationTest extends AbstractIntegrationTest {

    private static final String TROLLEY_ROUTE_URI = String.format("/api/v0/trolleys/%1$s/trolleyroutes", TROLLEY_ID);
    private static final String TROLLEY_ROUTE_URI_DELETE = "/api/v0/trolleyroutes";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllTrolleyRouteOfOneTrolleyReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(TROLLEY_ROUTE_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<TrolleyRouteDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), TrolleyRouteDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void createTrolleyRouteReturns200Test() throws Exception {

        TrolleyRouteDto requestDto = new TrolleyRouteDto();
        requestDto.setId(1234);
        requestDto.setTrolleyId(TROLLEY_ID);
        requestDto.setRouteOrder(1);
        requestDto.setShelfId(SHELF_ID);
        requestDto.setSortingDate(OffsetDateTime.now());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(TROLLEY_ROUTE_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createTrolleyRouteMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(TROLLEY_ROUTE_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createTrolleyRouteWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(TROLLEY_ROUTE_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteTrolleyRouteReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(TROLLEY_ROUTE_URI_DELETE + "/" + TROLLEY_ROUTE_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteTrolleyRouteNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(TROLLEY_ROUTE_URI_DELETE + "/" + TROLLEY_ROUTE_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
