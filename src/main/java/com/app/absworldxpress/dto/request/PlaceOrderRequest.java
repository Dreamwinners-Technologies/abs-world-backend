package com.app.absworldxpress.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceOrderRequest {
    private String customerPhoneNumber;
    private String deliveryAddress;
    private String orderNote;
    private String paymentMethod;

    private List<OrderProductModelRequest> productList;
}
