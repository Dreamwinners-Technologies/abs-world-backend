package com.app.absworldxpress.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String productId;
    private String productName;
    private String productSlug;
    private String SKU;
    private String productImage;

    private String categoryName;

    private Integer quantity;
    private Integer productPrice;

}
