package com.app.absworldxpress.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Slider_table")
public class SliderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private  String title;
    private String imageURL;
    private String linkedId;
}
