package com.app.absworldxpress.jwt.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import org.springframework.http.ResponseEntity;

public interface ForgetPasswordService {

    ResponseEntity<ApiMessageResponse> changePassword(Integer otp, String email, String newPassword);

    ResponseEntity<ApiMessageResponse> generateOTP(String email);
}
