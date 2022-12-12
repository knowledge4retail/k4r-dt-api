package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "facing")
public class Facing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "shelf_layer_id")
    private Integer shelfLayerId;
    @Column(name = "layer_relative_position")
    private Integer layerRelativePosition;
    @Column(name = "no_of_items_width")
    private Integer noOfItemsWidth;
    @Column(name = "no_of_items_depth")
    private Integer noOfItemsDepth;
    @Column(name = "no_of_items_height")
    private Integer noOfItemsHeight;
    @Column(name = "min_stock")
    private Integer minStock;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "misplaced_stock")
    private Integer misplacedStock;
    @Column(name = "product_unit_id")
    private Integer productUnitId;
    @Column(name = "external_reference_id")
    private String externalReferenceId;
}
