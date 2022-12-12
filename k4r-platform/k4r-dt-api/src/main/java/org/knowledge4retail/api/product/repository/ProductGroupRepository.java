package org.knowledge4retail.api.product.repository;

import org.knowledge4retail.api.product.model.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Integer>, JpaSpecificationExecutor<ProductGroup> {
    List<ProductGroup> findByStoreId(Integer storeId);
}
