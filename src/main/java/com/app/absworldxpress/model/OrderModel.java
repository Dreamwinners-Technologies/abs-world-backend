package com.app.absworldxpress.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderModel {
    @Id
    private String orderId;
    private String orderSKU;
    private String orderSlug;
    private String customerId;
    private String customerName;
    private String customerPhoneNumber;
    private String deliveryAddress;
    private String orderNote;

    private Integer orderAmount;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    private List<OrderProductModel> productModelList;

    private String createdBy;
    private Long creationTime;
    private String updatedBy;
    private Long updatedTime;
}
