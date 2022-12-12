package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;

@Data
public class StoreCharacteristicDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String name;
    private String code;
}
