package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.XmlProductImportDto;

import java.util.List;

public interface XMLService {

    List<ImportProductDto> importProducts(XmlProductImportDto xmlProductImportDto) throws Exception;

}
