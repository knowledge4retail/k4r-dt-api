package org.knowledge4retail.api.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.shared.dto.BasicDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScanDto implements BasicDto {

    @NotBlank
    private String timestamp;
    @NotBlank
    private String entityType;  // referenceFrame and referenceId for further steps
    @NotNull
    private String id;
    private String origin;
    private Double positionX;
    private Double positionY;
    private Double positionZ;
    private Double orientationX;
    private Double orientationY;
    private Double orientationZ;
    private Double orientationW;
    private Double height;
    private Double width;
    private Double depth;
    private Integer lengthUnitId;
    private String externalReferenceId;
    private String additionalInfo;
}
