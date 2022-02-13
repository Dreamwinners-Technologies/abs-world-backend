package com.app.absworldxpress.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliveryAreaModel {
    @Id
    private String deliveryAreaId;
    private String deliveryAreaName;
    private String district;
    private String division;
    private String country;
    private Integer deliveryCharge;
}
