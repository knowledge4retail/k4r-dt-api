package org.knowledge4retail.api.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.ProductListDto;
import org.knowledge4retail.api.product.dto.XmlProductImportDto;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.product.service.XMLService;
import org.springframework.stereotype.Service;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@Service
public class XMLServiceImpl implements XMLService {

    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public XMLServiceImpl(ObjectMapper objectMapper, ProductService productService){
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @Override
    public List<ImportProductDto> importProducts(XmlProductImportDto xmlProductImportDto) throws Exception{

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Source xslt = new StreamSource(new StringReader(xmlProductImportDto.getXslt()));
        Transformer transformer = factory.newTransformer(xslt);

        Source xml = new StreamSource(new StringReader(xmlProductImportDto.getXml()));
        StringWriter strWr = new StringWriter();
        transformer.transform(xml, new StreamResult(strWr));
        String json = strWr.toString();

        log.debug("XSLT Transformation Result: " + json);

        ProductListDto productList = objectMapper.readValue(json, ProductListDto.class);
        return productService.createMany(productList.getProducts());
    }
}
