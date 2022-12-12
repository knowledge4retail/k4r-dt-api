package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class DeliveredUnitDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @Schema(description = "FK")
    private Integer trolleyId;
    @NotNull
    @Schema(description = "FK.NN")
    private Integer productUnitId;
    @NotNull
    @Schema(description = "FK.NN")
    private Integer productGtinId;
    private Integer trolleyLayer;
    @NotNull
    @Schema(description = "NN")
    private String palletId;
    private String sortingState;
    private OffsetDateTime sortingDate;
    @NotNull
    @Schema(description = "NN")
    private Integer amountUnit;
    @NotNull
    @Schema(description = "NN")
    private Integer amountItems;
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
