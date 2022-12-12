package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Planogram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanogramRepository extends JpaRepository<Planogram, Integer>, JpaSpecificationExecutor<Planogram> {

    List<Planogram> findByStoreId(Integer storeId);
}
