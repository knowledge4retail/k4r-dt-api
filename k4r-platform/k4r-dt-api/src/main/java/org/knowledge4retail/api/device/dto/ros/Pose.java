package org.knowledge4retail.api.device.dto.ros;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pose {

    @NotNull
    @Valid
    private Position position;

    @NotNull
    @Valid
    private Orientation orientation;
}
