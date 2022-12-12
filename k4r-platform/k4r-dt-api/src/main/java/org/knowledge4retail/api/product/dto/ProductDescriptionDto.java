package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

@Data
public class ProductDescriptionDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @Schema(description = "FK")
    private String productId;
    private String description;
    private String isoLanguageCode;
}
