package org.knowledge4retail.api.scan.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ScanKey implements Serializable {

    @NotBlank
    private String entityType;  // referenceFrame and referenceId for further steps
    @NotNull
    private OffsetDateTime timestamp;
    @NotNull
    private String id;
}
