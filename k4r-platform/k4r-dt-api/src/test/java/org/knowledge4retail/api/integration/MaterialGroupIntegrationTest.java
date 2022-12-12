package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.MaterialGroupDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class MaterialGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String MATERIAL_GROUP_URI = "/api/v0/materialgroups";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllMaterialGroupsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(MATERIAL_GROUP_URI)
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<MaterialGroupDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), MaterialGroupDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getName());
    }

    @Test
    public void getMaterialGroupReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(MATERIAL_GROUP_URI + "/" + MATERIAL_GROUP_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        MaterialGroupDto content = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), MaterialGroupDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
        Assertions.assertNotNull(content.getName());
    }

    @Test
    public void createMaterialGroupReturns200Test() throws Exception {

        MaterialGroupDto requestDto = new MaterialGroupDto();
        requestDto.setName("TestMaterialGroup");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(MATERIAL_GROUP_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createMaterialGroupMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(MATERIAL_GROUP_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createMaterialGroupWrongReturns400BodyTest() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(MATERIAL_GROUP_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }


    @Test
    public void createMaterialGroupMissingNameReturns400Test() throws Exception {

        MaterialGroupDto requestDto = new MaterialGroupDto();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(MATERIAL_GROUP_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateMaterialGroupReturns200Test() throws Exception {

        MaterialGroupDto requestDto = new MaterialGroupDto();
        requestDto.setName("test");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(MATERIAL_GROUP_URI + "/" + MATERIAL_GROUP_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateMaterialGroupWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(MATERIAL_GROUP_URI + "/" + MATERIAL_GROUP_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteMaterialGroupReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(MATERIAL_GROUP_URI + "/" + MATERIAL_GROUP_ID_DELETE)
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteMaterialGroupMissingIdReturns405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(MATERIAL_GROUP_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteMaterialGroupNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(MATERIAL_GROUP_URI + "/" + MATERIAL_GROUP_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
