package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<ApiResponse<String>> initialPayment(String token, String orderId);
}
