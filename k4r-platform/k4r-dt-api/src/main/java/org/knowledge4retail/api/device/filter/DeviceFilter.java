package org.knowledge4retail.api.device.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DeviceFilter {

    private FilterField id;
    private FilterField storeId;
    private FilterField deviceType;
    private FilterField description;
}
