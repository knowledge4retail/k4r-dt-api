package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "store_property")
public class StoreProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "characteristic_id")
    private Integer characteristicId;
    @Column(name = "store_id")
    private Integer storeId;
    @NotBlank
    @Column(name = "value_low")
    private String valueLow;
    @Column(name = "value_high")
    private String valueHigh;
}
