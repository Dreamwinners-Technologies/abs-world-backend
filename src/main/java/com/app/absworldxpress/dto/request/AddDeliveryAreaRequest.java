package com.app.absworldxpress.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddDeliveryAreaRequest {
    private String deliveryAreaName;
    private String district;
    private String division;
    private String country;
    private Integer deliveryCharge;
}
