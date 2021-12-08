package com.app.absworldxpress.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddReviewRequest {
    private Double rating;
    private String comment;
}
