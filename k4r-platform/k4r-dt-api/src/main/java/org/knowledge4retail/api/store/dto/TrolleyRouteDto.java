package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class TrolleyRouteDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;
    @NotNull
    @Schema(description = "FK.NN")
    private Integer trolleyId;
    @NotNull
    @Schema(description = "NN")
    private OffsetDateTime sortingDate;
    @NotNull
    @Schema(description = "NN")
    private Integer routeOrder;
    @NotNull
    @Schema(description = "FK.NN")
    private Integer shelfId;
}
