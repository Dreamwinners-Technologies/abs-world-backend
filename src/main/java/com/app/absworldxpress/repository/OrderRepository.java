package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel,String > {
    Optional<OrderModel> findByTransactionIdIgnoreCase(String TransactionId);

}
