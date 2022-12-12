package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="shopping_basket_position")
public class ShoppingBasketPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "product_id")
    private String productId;

    @NotNull
    @Column(name = "store_id")
    private Integer storeId;

    @NotNull
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name= "currency")
    private String currency;

}
