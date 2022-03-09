package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.PlaceOrderRequest;
import com.app.absworldxpress.dto.response.OrderListResponse;
import com.app.absworldxpress.model.OrderModel;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponse<OrderModel>> placeOrder(String token, PlaceOrderRequest placeOrderRequest);

    ResponseEntity<ApiResponse<OrderListResponse>> getOrderList(String token, String createdBy, String orderId, String orderSKU, String customerPhoneNumber, String sortBy, Sort.Direction orderBy, int pageSize, int pageNo);

    ResponseEntity<ApiMessageResponse> cancelOrder(String token, String orderId);
}
