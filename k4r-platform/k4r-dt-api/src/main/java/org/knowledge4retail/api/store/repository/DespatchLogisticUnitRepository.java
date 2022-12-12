package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.DespatchLogisticUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DespatchLogisticUnitRepository extends JpaRepository<DespatchLogisticUnit, Integer>, JpaSpecificationExecutor<DespatchLogisticUnit> {

    List<DespatchLogisticUnit> findByDespatchAdviceId(Integer despatchAdviceId);
}
