package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.ShoppingBasketPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasketPosition, Integer>, JpaSpecificationExecutor<ShoppingBasketPosition> {

    List<ShoppingBasketPosition> findByStoreIdAndCustomerId(Integer storeId, Integer customerId);
}
