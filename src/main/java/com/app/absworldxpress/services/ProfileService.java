package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.EditProfileRequest;
import com.app.absworldxpress.dto.response.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(String token);

    ResponseEntity<ApiMessageResponse> editProfile(String token, EditProfileRequest editProfileRequest);

    ResponseEntity<ApiMessageResponse> uploadProfileImage(String token, MultipartFile aFile);
}
