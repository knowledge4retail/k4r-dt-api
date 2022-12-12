package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Sales;
import org.knowledge4retail.api.store.model.SalesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, SalesKey>, JpaSpecificationExecutor<Sales> {

    Boolean existsByStoreIdAndGtin(Integer storeId, String gtin);

    List<Sales> findByStoreIdAndGtin(Integer storeId, String gtin);
}