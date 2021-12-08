package com.app.absworldxpress.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewModel {
    @Id
    private String reviewId;
    private Double rating;
    private String reviewerName;
    private String comment;

    private String createdBy;
    private Long creationTime;
}
