package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.PaymentInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfoModel,String > {
}
