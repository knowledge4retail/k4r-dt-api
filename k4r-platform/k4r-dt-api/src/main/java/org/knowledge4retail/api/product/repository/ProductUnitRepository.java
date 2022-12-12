package org.knowledge4retail.api.product.repository;

import org.knowledge4retail.api.product.model.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Integer>, JpaSpecificationExecutor<ProductUnit> {}
