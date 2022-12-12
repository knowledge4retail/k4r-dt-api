package org.knowledge4retail.api.wireframe.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class WireframeFilter {

    private FilterField id;
    private FilterField gTIN;
    private FilterField timestamp;
    private FilterField dataFormat;
}
