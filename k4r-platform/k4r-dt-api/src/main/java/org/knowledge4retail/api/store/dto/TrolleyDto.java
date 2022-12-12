package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;

@Data
public class TrolleyDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @NotNull
    @Schema(description = "FK.NN")
    private Integer storeId;
    @NotNull
    @Schema(description = "NN")
    private Integer layers;
    @NotNull
    @Schema(description = "NN")
    private Double width;
    @NotNull
    @Schema(description = "NN")
    private Double height;
    @NotNull
    @Schema(description = "NN")
    private Double depth;
}
