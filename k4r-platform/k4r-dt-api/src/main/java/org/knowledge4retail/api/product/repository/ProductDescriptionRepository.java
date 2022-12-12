package org.knowledge4retail.api.product.repository;

import org.knowledge4retail.api.product.model.ProductDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Integer>, JpaSpecificationExecutor<ProductDescription> {

    List<ProductDescription> findByProductId(String productId);
}
