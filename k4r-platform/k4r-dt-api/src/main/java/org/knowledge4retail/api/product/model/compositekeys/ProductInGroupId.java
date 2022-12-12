package org.knowledge4retail.api.product.model.compositekeys;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductInGroupId implements Serializable {
    @NotEmpty
    private String productId;
    @NotNull
    private Integer productGroupId;
}
