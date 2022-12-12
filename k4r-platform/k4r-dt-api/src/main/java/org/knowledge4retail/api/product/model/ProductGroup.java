package org.knowledge4retail.api.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product_group")
public class ProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Column(name="name")
    private String name;
    @Column(name="store_id")
    private Integer storeId;

    @ManyToMany
    @JoinTable(
            name = "product_in_group",
            joinColumns = @JoinColumn(name="product_group_id"),
            inverseJoinColumns = @JoinColumn(name="product_id"))
    @ToString.Exclude
    private List<Product> products;
}
