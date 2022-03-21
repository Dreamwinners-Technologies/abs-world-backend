package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.model.PaymentInfoModel;
import com.app.absworldxpress.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/success/{object}")
    public ResponseEntity<ApiMessageResponse> paymentSuccess(@PathVariable(required = false) Object object){
        return new ResponseEntity<>(new ApiMessageResponse(200,"Payment Successful!"), HttpStatus.OK);
    }

    @PostMapping("/fail/{object}")
    public ResponseEntity<ApiMessageResponse> paymentFailed(@PathVariable(required = false) Object object){
        return new ResponseEntity<>(new ApiMessageResponse(400,"Payment Failed!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/cancel/{object}")
    public ResponseEntity<ApiMessageResponse> paymentCanceled(@PathVariable(required = false) Object object){
        return new ResponseEntity<>(new ApiMessageResponse(200,"Payment Canceled!"), HttpStatus.OK);
    }

    @PostMapping("/ipnListener")
    public ResponseEntity<ApiResponse<String>> ipnListener (
            @RequestParam(required = false, defaultValue = "") String tran_id, @RequestParam(required = false, defaultValue = "") String val_id,
            @RequestParam(required = false, defaultValue = "") String amount, @RequestParam(required = false, defaultValue = "") String card_type,
            @RequestParam(required = false, defaultValue = "") String store_amount, @RequestParam(required = false, defaultValue = "") String card_no,
            @RequestParam(required = false, defaultValue = "") String bank_tran_id, @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String tran_date, @RequestParam(required = false, defaultValue = "") String currency,
            @RequestParam(required = false, defaultValue = "") String card_issuer, @RequestParam(required = false, defaultValue = "") String card_brand,
            @RequestParam(required = false, defaultValue = "") String card_issuer_country, @RequestParam(required = false, defaultValue = "") String card_issuer_country_code,
            @RequestParam(required = false, defaultValue = "") String store_id, @RequestParam(required = false, defaultValue = "") String verify_sign,
            @RequestParam(required = false, defaultValue = "") String verify_key, @RequestParam(required = false, defaultValue = "") String currency_type,
            @RequestParam(required = false, defaultValue = "") String currency_amount, @RequestParam(required = false, defaultValue = "") String currency_rate,
            @RequestParam(required = false, defaultValue = "") String base_fair, @RequestParam(required = false, defaultValue = "") String value_a,
            @RequestParam(required = false, defaultValue = "") String value_b,
            @RequestParam(required = false, defaultValue = "") String value_c, @RequestParam(required = false, defaultValue = "") String value_d,
            @RequestParam(required = false, defaultValue = "") String risk_level, @RequestParam(required = false, defaultValue = "") String risk_title) throws Exception {

        PaymentInfoModel paymentInfoModel = new PaymentInfoModel(UUID.randomUUID().toString(),
                status, tran_date, tran_id, val_id, amount, store_amount, currency, bank_tran_id,
                card_type, card_no, card_issuer, card_brand, card_issuer_country, card_issuer_country_code, currency_type,
                currency_amount, currency_rate, base_fair, value_a, value_b, value_c,
                value_d, "", "", "", verify_sign, verify_key, risk_level, risk_title, store_id);

        return paymentService.ipnListener(paymentInfoModel);
    }
}
