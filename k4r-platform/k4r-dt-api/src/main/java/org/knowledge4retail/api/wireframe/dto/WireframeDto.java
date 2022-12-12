package org.knowledge4retail.api.wireframe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class WireframeDto implements BasicDto {

    @Schema(description = "PK.autoincrement", hidden = true)
    private Integer id;

    @NotBlank
    @Schema(required = true, description = "notBlank")
    private String gTIN;

    @Schema(hidden = true)
    private OffsetDateTime timestamp;

    @Schema(hidden = true)
    private String dataFormat;

    @Schema(hidden = true)
    private byte[] Wireframe;
}
