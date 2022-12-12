package org.knowledge4retail.api.store.dto;

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
public class SalesDto implements BasicDto {

    @NotBlank
    private String timestamp;
    @NotBlank
    private String gtin;  // referenceFrame and referenceId for further steps
    @NotNull
    private Integer storeId;
    private Double soldUnits;
    private Double turnover;
    private String timeResolution;
    private String type;
}
