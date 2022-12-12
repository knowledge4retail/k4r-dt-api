package org.knowledge4retail.api.store.model;

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
public class SalesKey implements Serializable {

    @NotBlank
    private String gtin;
    @NotNull
    private OffsetDateTime timestamp;
    @NotNull
    private Integer storeId;
}
