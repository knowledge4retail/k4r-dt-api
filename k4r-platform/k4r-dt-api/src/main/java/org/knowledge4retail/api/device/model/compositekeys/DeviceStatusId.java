package org.knowledge4retail.api.device.model.compositekeys;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatusId implements Serializable {

    @NotEmpty
    private String deviceId;

    @NotNull
    private Long timestamp;

}
