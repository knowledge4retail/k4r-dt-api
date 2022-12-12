package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ProductListDto {
    @NotEmpty
    @Schema(required = true, description = "notEmpty")
    private List<ImportProductDto> products;
}
