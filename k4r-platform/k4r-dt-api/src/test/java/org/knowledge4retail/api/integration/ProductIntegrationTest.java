package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.*;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.knowledge4retail.api.test.Data.*;

public class ProductIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCTS = "/api/v0/products";
    private static final String PRODUCTLIST = "/api/v0/products/list";
    private static final String PRODUCT_IMPORT = "/api/v0/products/import";
    private static final String PROPERTIES_URI = "/api/v0/products/TST-020/productproperties";

    private static final String XSLT_CONTENT = "<xsl:stylesheet version=\"1.0\"xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"xmlns=\"http://www.w3.org/TR/REC-html40\"></xsl:stylesheet>";
    private static final String XML_CONTENT = "<?xml version=\"1.0\"?>";


    @BeforeEach
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }

    @Test
    public void getProductsTest() throws Exception {
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCTS)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = itemsMvcResult.getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        List<ProductDto> contentList = mapListFromJson(itemsMvcResult.getResponse().getContentAsString(), ProductDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getMaterialGroupId());
    }

    @Test
    public void getProductTest() throws Exception {
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCTS + "/" + PRODUCT_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        ProductDto productDto = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), ProductDto.class);

        Assertions.assertNotNull(productDto);
        Assertions.assertNotNull(productDto.getId());
    }

    @Test
    public void getProductWIthInvalidIdTest() throws Exception {
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCTS + "/"+ PRODUCT_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, itemsMvcResult);
    }

    @Test
    public void updateProductTest() throws Exception {
        ProductDto requestDto = new ProductDto();
        requestDto.setId(PRODUCT_ID);
        requestDto.setProductBaseUnit("Testprodukt 3");

        MvcResult  itemsMvcResult = mvc.perform(MockMvcRequestBuilders.put(PRODUCTS + "/" + PRODUCT_ID).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = itemsMvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);

        itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCTS + "/"+ PRODUCT_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        ProductDto productDto = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), ProductDto.class);

        Assertions.assertEquals("Testprodukt 3", productDto.getProductBaseUnit());
    }

    @Test
    public void updateProductWithInvalidId()throws Exception{
        ProductDto requestDto = new ProductDto();
        requestDto.setProductBaseUnit("Testprodukt 9876");
        requestDto.setId("TST-9876");
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.put(PRODUCTS + "/"+ PRODUCT_ID_404).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, itemsMvcResult);
    }

    @Test
    public void createProductTest() throws Exception {
        ProductDto requestDto = new ProductDto();
        requestDto.setId("TST-1005");

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCTS).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);
    }

    @Test
    public void createProductWithExistingIdTest() throws Exception {
        ProductDto requestDto = new ProductDto();
        requestDto.setId(PRODUCT_ID);

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCTS).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void createProductWithMissingId() throws Exception {
        ProductDto requestDto = new ProductDto();

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCTS).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void createProductsTest() throws Exception {
        ImportProductDto requestDto1 = new ImportProductDto();
        requestDto1.setId("TST-010");

        ImportProductDto requestDto2 = new ImportProductDto();
        requestDto2.setId("TST-011");

        List<ImportProductDto> products = new ArrayList<>();
        products.add(requestDto1);
        products.add(requestDto2);
        ProductListDto productListDto = new ProductListDto();
        productListDto.setProducts(products);

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCTLIST).contentType(JSON_CONTENT_TYPE).content(mapToJson(productListDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);
    }

    @Test
    public void createProductsWithPropertiesTest() throws Exception {
        ImportProductDto requestDto1 = new ImportProductDto();
        requestDto1.setId("TST-020");

        List<ImportProductDto> products = new ArrayList<>();
        ProductPropertyDto productPropertyDto = new ProductPropertyDto();
        productPropertyDto.setCharacteristicId(1000);
        productPropertyDto.setProductId("TST-020");
        productPropertyDto.setValueLow("JustAnotherTestProperty");
        List<ProductPropertyDto> properties = new ArrayList<>();
        properties.add(productPropertyDto);
        requestDto1.setProperties(properties);
        products.add(requestDto1);
        ProductListDto productListDto = new ProductListDto();
        productListDto.setProducts(products);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCTLIST).contentType(JSON_CONTENT_TYPE).content(mapToJson(productListDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.OK.value(), status);

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(PROPERTIES_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        status = mvcResult.getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
        List<ProductPropertyDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), ProductPropertyDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void deleteProductWithInvalidId() throws Exception {
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCTS + "/" + PRODUCT_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, itemsMvcResult);
    }

    @Test
    public void deleteProduct() throws Exception {
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCTS + "/" + PRODUCT_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCTS + "/" + PRODUCT_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, itemsMvcResult);
    }

    @Test
    public void testXmlImportThrowsErrorForEmptyXml() throws Exception {
        XmlProductImportDto xmlProductImportDto = new XmlProductImportDto();
        xmlProductImportDto.setXml("");
        xmlProductImportDto.setXslt(XSLT_CONTENT);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_IMPORT).contentType(JSON_CONTENT_TYPE).content(mapToJson(xmlProductImportDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void testXmlImportThrowsErrorForEmptyXslt() throws Exception {
        XmlProductImportDto xmlProductImportDto = new XmlProductImportDto();
        xmlProductImportDto.setXml(XML_CONTENT);
        xmlProductImportDto.setXslt("");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_IMPORT).contentType(JSON_CONTENT_TYPE).content(mapToJson(xmlProductImportDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void testXmlImport() throws Exception {
        URL xmlFileUrl = ProductIntegrationTest.class.getResource("testimport.xml");
        URL xsltFileUrl = ProductIntegrationTest.class.getResource("testimport.xsl");
        String xmlString = Files.readString(java.nio.file.Paths.get(Objects.requireNonNull(xmlFileUrl).toURI()));
        String xsltString = Files.readString(java.nio.file.Paths.get(Objects.requireNonNull(xsltFileUrl).toURI()));
        XmlProductImportDto xmlProductImportDto = new XmlProductImportDto();
        xmlProductImportDto.setXml(xmlString);
        xmlProductImportDto.setXslt(xsltString);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_IMPORT).contentType(JSON_CONTENT_TYPE).content(mapToJson(xmlProductImportDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    /*
    ------   ProductPropertyDto -> ProductCharacteristic @NotNull, sodass hier testimport_error.xsl gezwungen wird
    richtig zu sein ------
    @Test
    public void testXmlImportThrowsErrorForInvalidTransformationResult() throws Exception {
        URL xmlFileUrl = ProductIntegrationTest.class.getResource("testimport.xml");
        URL xsltFileUrl = ProductIntegrationTest.class.getResource("testimport_error.xsl");
        String xmlString = Files.readString(java.nio.file.Paths.get(xmlFileUrl.toURI()));
        String xsltString = Files.readString(java.nio.file.Paths.get(xsltFileUrl.toURI()));
        XmlProductImportDto xmlProductImportDto = new XmlProductImportDto();
        xmlProductImportDto.setXml(xmlString);
        xmlProductImportDto.setXslt(xsltString);
        System.out.println("hier " + mapToJson(xmlProductImportDto));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_IMPORT).contentType(JSON_CONTENT_TYPE).content(mapToJson(xmlProductImportDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }*/

    @Test
    public void testXmlImportThrowsErrorForInvalidXML() throws Exception {
        URL xmlFileUrl = ProductIntegrationTest.class.getResource("testimport_error.xml");
        URL xsltFileUrl = ProductIntegrationTest.class.getResource("testimport.xsl");
        String xmlString = Files.readString(java.nio.file.Paths.get(Objects.requireNonNull(xmlFileUrl).toURI()));
        String xsltString = Files.readString(java.nio.file.Paths.get(Objects.requireNonNull(xsltFileUrl).toURI()));
        XmlProductImportDto xmlProductImportDto = new XmlProductImportDto();
        xmlProductImportDto.setXml(xmlString);
        xmlProductImportDto.setXslt(xsltString);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_IMPORT).contentType(JSON_CONTENT_TYPE).content(mapToJson(xmlProductImportDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }
}
