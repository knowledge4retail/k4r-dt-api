package org.knowledge4retail.api.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class PlanogramMessageDto implements BasicDto {

    @Schema(description = "PK.autoincrement")
    private Integer id;

    @NotNull
    @Schema(required = true, description = "FK.notNull")
    private Integer storeId;

    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String referenceId;

    @Schema(hidden = true)
    private OffsetDateTime timestamp;

    private String dataFormat;
}
