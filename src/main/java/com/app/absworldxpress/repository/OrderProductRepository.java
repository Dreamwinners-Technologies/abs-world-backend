package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.OrderProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductModel,Long> {

}
