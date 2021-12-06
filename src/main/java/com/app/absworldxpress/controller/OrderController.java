package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.PlaceOrderRequest;
import com.app.absworldxpress.dto.response.OrderListResponse;
import com.app.absworldxpress.model.OrderModel;
import com.app.absworldxpress.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderModel>> placeOrder(@RequestHeader(name = "Authorization") String token,
                                                              @RequestBody PlaceOrderRequest placeOrderRequest){
        return orderService.placeOrder(token,placeOrderRequest);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrderList(@RequestHeader(name = "Authorization") String token,
                                                                       @RequestParam(required = false) String createdBy,
                                                                       String orderId, String orderSKU, String customerPhoneNumber,
                                                                       @RequestParam(defaultValue = "creationTime") String sortBy,
                                                                       @RequestParam(defaultValue = "ASC") Sort.Direction orderBy,
                                                                       @RequestParam(defaultValue = "20") int pageSize,
                                                                       @RequestParam(defaultValue = "0") int pageNo){
        return orderService.getOrderList(token,createdBy,orderId,orderSKU,customerPhoneNumber,sortBy,orderBy,pageSize,pageNo);
    }
}
