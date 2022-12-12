package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.DespatchAdvice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DespatchAdviceRepository extends JpaRepository<DespatchAdvice, Integer>, JpaSpecificationExecutor<DespatchAdvice> {

    List<DespatchAdvice> findByStoreId(Integer storeId);
}
