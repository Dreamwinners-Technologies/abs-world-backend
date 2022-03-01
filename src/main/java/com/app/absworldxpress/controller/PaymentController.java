package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse<String>> initialPayment(@RequestHeader(name = "Authorization") String token,
                                                              @PathVariable String orderId) throws Exception{
        return paymentService.initialPayment(token,orderId);
    }
}
