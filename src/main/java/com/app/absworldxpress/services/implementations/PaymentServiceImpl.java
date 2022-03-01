package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public ResponseEntity<ApiResponse<String>> initialPayment(String token, String orderId) {
        return null;
    }
}
