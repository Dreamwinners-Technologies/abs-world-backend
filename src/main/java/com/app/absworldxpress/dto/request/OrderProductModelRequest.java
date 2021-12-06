package com.app.absworldxpress.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderProductModelRequest {
    @NotBlank
    private String productId;

    @Positive(message = "Quantity can't be less than 1")
    private Integer quantity;
}
