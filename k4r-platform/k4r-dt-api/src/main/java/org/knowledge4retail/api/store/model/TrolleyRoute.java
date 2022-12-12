package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "trolley_route")
public class TrolleyRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "trolley_id")
    private Integer trolleyId;

    @NotNull
    @Column(name = "sorting_date")
    private OffsetDateTime sortingDate;

    @NotNull
    @Column(name = "route_order")
    private Integer routeOrder;

    @NotNull
    @Column(name = "shelf_id")
    private Integer shelfId;
}
