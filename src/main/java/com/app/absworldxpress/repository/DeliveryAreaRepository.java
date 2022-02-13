package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.DeliveryAreaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAreaRepository extends JpaRepository<DeliveryAreaModel,String> {
}
