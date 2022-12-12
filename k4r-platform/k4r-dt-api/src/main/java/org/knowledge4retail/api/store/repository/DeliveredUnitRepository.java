package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveredUnitRepository extends JpaRepository<DeliveredUnit, Integer>, JpaSpecificationExecutor<DeliveredUnit> {}
