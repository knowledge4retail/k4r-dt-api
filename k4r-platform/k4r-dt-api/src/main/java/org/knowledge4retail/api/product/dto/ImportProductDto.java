package org.knowledge4retail.api.product.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImportProductDto extends ProductDto{

    private List<ProductPropertyDto> properties;
}
