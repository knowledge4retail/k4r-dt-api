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
@Table(name = "despatch_advice")
public class DespatchAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "store_id")
    private Integer storeId;
    @Column(name = "ship_time")
    private OffsetDateTime shipTime;
    @Column(name = "creation_time")
    private OffsetDateTime creationTime;
    @Column(name = "sender_qualifier")
    private String senderQualifier;
    @Column(name = "sender_id")
    private String senderId;
    @Column(name = "document_number")
    private String documentNumber;
}
