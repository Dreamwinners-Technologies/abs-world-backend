package com.app.absworldxpress.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserProfileResponse {

        private String fullName;

        private String username;

        private String email;

        String phoneNo;

        String deliveryAddress;

        String profileImage;
}
