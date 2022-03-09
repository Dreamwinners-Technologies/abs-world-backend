package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.model.PaymentInfoModel;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<ApiResponse<String>> initialPayment(String token, String orderId) throws Exception;

    ResponseEntity<ApiResponse<String>> ipnListener(PaymentInfoModel paymentInfoModel) throws Exception;
}
