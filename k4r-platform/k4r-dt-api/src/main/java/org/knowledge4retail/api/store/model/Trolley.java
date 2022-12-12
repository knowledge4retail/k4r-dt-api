package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "trolley")
public class Trolley {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "store_id")
    private Integer storeId;

    @NotNull
    @Column(name = "layers")
    private Integer layers;

    @NotNull
    @Column(name = "width")
    private Double width;

    @NotNull
    @Column(name = "height")
    private Double height;

    @NotNull
    @Column(name = "depth")
    private Double depth;
}
