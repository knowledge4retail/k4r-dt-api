package org.knowledge4retail.api.product.model;

import lombok.*;
import org.knowledge4retail.api.product.model.compositekeys.ProductInGroupId;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@IdClass(ProductInGroupId.class)
@Table(name = "product_in_group")
public class ProductInGroup {
    @Id
    @Column(name = "product_id")
    private String productId;
    @Id
    @Column(name = "product_group_id")
    private Integer productGroupId;
}

