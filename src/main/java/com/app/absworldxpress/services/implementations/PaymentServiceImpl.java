package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.model.OrderModel;
import com.app.absworldxpress.model.OrderProductModel;
import com.app.absworldxpress.model.PaymentInfoModel;
import com.app.absworldxpress.repository.OrderRepository;
import com.app.absworldxpress.services.PaymentService;
import com.app.absworldxpress.sslCommerz.SSLCommerz;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.app.absworldxpress.sslCommerz.SSLCommerz;

import java.util.*;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<String>> initialPayment(String token, String orderId) throws Exception {

        Optional<OrderModel> orderModelOptional = orderRepository.findById(orderId);

        if (orderModelOptional.isPresent()) {
            String trxId = "ABS-T-" + UUID.randomUUID().toString().toUpperCase().substring(0, 11);

            OrderModel orderModel = orderModelOptional.get();

            orderModel.setTransactionId(trxId);

            SSLCommerz sslCommerz = new SSLCommerz("absxp61d5d433355ef", "absxp61d5d433355ef@ssl", true);

            StringBuilder products = new StringBuilder();
            for (OrderProductModel orderProductModel : orderModel.getProductModelList()) {
                products.append(orderProductModel.getProductName()).append(",");
            }

            String email;
            User customer = userRepository.getById(orderModel.getCustomerId());
            if (customer.getEmail() != null) {
                email = customer.getEmail();
            } else {
                email = "absworldexpress@gmail.com";
            }

            Map<String, String> postData = new HashMap<>();
            if (orderModel.getPaymentMethod().equals("COD")) {
                postData.put("total_amount", orderModel.getDeliveryAreaModel().getDeliveryCharge().toString());
            }
            else {
                postData.put("total_amount", orderModel.getOrderAmount().toString());
            }

            postData.put("currency", "BDT");
            postData.put("tran_id", trxId);
            postData.put("product_category", "Fashion");
            postData.put("success_url", "https://payment-abs-success.tiiny.site/success.html");
            postData.put("fail_url", "https://e-store-ui.vercel.app/");
            postData.put("cancel_url", "https://payment-abs-success.tiiny.site/cancel.html/");
            postData.put("emi_option", "0");
            postData.put("cus_name", orderModel.getCustomerName());
            postData.put("cus_email", email);
            postData.put("cus_add1", orderModel.getDeliveryAddress());
            postData.put("cus_city", orderModel.getDeliveryAddress());
            postData.put("cus_postcode", "");
            postData.put("cus_country", "Bangladesh");
            postData.put("cus_phone", orderModel.getCustomerPhoneNumber());
            postData.put("shipping_method", "Courier");
            postData.put("num_of_item", String.valueOf(orderModel.getProductModelList().size()));
            postData.put("product_name", products.substring(0, products.length() - 1));
            postData.put("product_profile", "general");


            System.out.println(postData.toString());

            String paymentLink = sslCommerz.initiateTransaction(postData, false);

            orderRepository.save(orderModel);

            return new ResponseEntity<>(new ApiResponse<>(200, "Payment Link Generated", paymentLink), HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Order Found with id: " + orderId);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<String>> ipnListener(PaymentInfoModel paymentInfoModel) throws Exception {
        SSLCommerz sslCommerz = new SSLCommerz("absxp61d5d433355ef", "absxp61d5d433355ef@ssl", true);

        Map<String, String> formData = new HashMap<>();
        formData.put("tran_id", paymentInfoModel.getTran_id());
        formData.put("val_id", paymentInfoModel.getVal_id());
        formData.put("amount", paymentInfoModel.getAmount());
        formData.put("card_type", paymentInfoModel.getCard_type());
        formData.put("store_amount", paymentInfoModel.getStore_amount());
        formData.put("card_no", paymentInfoModel.getCard_no());
        formData.put("bank_tran_id", paymentInfoModel.getBank_tran_id());
        formData.put("tran_date", paymentInfoModel.getTran_date());
        formData.put("currency", paymentInfoModel.getCurrency());
//        formData.put("bank_tran_id", paymentInfoModel.getBank_tran_id());
        formData.put("card_issuer", paymentInfoModel.getCard_issuer());
        formData.put("card_brand", paymentInfoModel.getCard_brand());
        formData.put("card_issuer_country", paymentInfoModel.getCard_issuer_country());
        formData.put("card_issuer_country_code", paymentInfoModel.getCard_issuer_country_code());
        formData.put("store_id", paymentInfoModel.getStore_id());
        formData.put("verify_sign", paymentInfoModel.getVerify_sign());
        formData.put("verify_key", paymentInfoModel.getVerify_key());
//        formData.put("cus_fax", paymentInfoModel.getCus_fax());
        formData.put("currency_type", paymentInfoModel.getCurrency_type());
        formData.put("currency_amount", paymentInfoModel.getCurrency_amount());
        formData.put("currency_rate", paymentInfoModel.getCurrency_rate());
        formData.put("value_a", paymentInfoModel.getValue_a());
        formData.put("value_b", paymentInfoModel.getValue_b());
        formData.put("value_c", paymentInfoModel.getValue_c());
        formData.put("value_d", paymentInfoModel.getValue_d());
        formData.put("risk_level", paymentInfoModel.getRisk_level());
        formData.put("risk_title", paymentInfoModel.getRisk_title());

        formData.put("base_fair", paymentInfoModel.getBase_fair());
        formData.put("status", paymentInfoModel.getStatus());

        System.out.println(paymentInfoModel.toString());

        boolean isVerified = sslCommerz.orderValidate(paymentInfoModel.getTran_id(),
                paymentInfoModel.getAmount(), paymentInfoModel.getCurrency(), formData);

        if(isVerified){
            Optional<OrderModel> orderModelOptional = orderRepository.findByTransactionIdIgnoreCase(paymentInfoModel.getTran_id());

            if(orderModelOptional.isPresent()){
                OrderModel orderModel = orderModelOptional.get();

                orderModel.setPaymentStatus("PAID");
                orderModel.setPaidAmount(Integer.valueOf(paymentInfoModel.getAmount().split("\\.")[0]));
                orderModel.setPaymentMethod(paymentInfoModel.getCard_brand());
                orderModel.setRiskLevel(paymentInfoModel.getRisk_level());
                orderModel.setRiskTitle(paymentInfoModel.getRisk_title());

                orderRepository.save(orderModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Payment Successful", null), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new ApiResponse<>(400, "Payment UnSuccessful", null), HttpStatus.BAD_REQUEST);
    }
}
