package org.knowledge4retail.api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class XmlProductImportDto {
    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String xml;
    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String xslt;
}
