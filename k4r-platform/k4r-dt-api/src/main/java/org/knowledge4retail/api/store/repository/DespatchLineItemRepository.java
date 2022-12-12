package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.DespatchLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DespatchLineItemRepository extends JpaRepository<DespatchLineItem, Integer>, JpaSpecificationExecutor<DespatchLineItem> {

    List<DespatchLineItem> findByDespatchLogisticUnitId(Integer despatchLogisticUnitId);
}
