package org.knowledge4retail.api.device.dto.ros;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    @NotNull
    private Double x;

    @NotNull
    private Double y;

    @NotNull
    private Double z;
}
