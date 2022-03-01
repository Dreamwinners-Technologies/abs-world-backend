package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.model.OrderModel;
import com.app.absworldxpress.model.OrderProductModel;
import com.app.absworldxpress.repository.OrderRepository;
import com.app.absworldxpress.services.PaymentService;
import com.app.absworldxpress.sslCommerz.SSLCommerz;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<String>> initialPayment(String token, String orderId) throws Exception {
        Optional<OrderModel> orderModelOptional = orderRepository.findById(orderId);

        if (orderModelOptional.isPresent()) {
            String trxId = "ABS-T-" + UUID.randomUUID().toString().toUpperCase().substring(0, 11);

            OrderModel orderModel = orderModelOptional.get();

            orderModel.setTransactionId(trxId);

            SSLCommerz sslCommerz = new SSLCommerz("dokan6070563821cf9", "dokan6070563821cf9@ssl", true);

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
            postData.put("total_amount", orderModel.getOrderAmount().toString());
            postData.put("currency", "BDT");
            postData.put("tran_id", trxId);
            postData.put("product_category", "Gadgets");
            postData.put("success_url", "https://www.facebook.com/absworldxpress");
            postData.put("fail_url", "https://ihsonnet.netlify.com");
            postData.put("cancel_url", "https://dokanee.com");
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
}
