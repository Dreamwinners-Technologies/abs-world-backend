package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.SettingsRequest;
import com.app.absworldxpress.model.SettingsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SettingsService {
    ResponseEntity<ApiMessageResponse> initSettings(String token);

    ResponseEntity<ApiMessageResponse> uploadLogo(String token, MultipartFile aFile);

    ResponseEntity<ApiResponse<SettingsModel>> getSettings();

    ResponseEntity<ApiResponse<SettingsModel>> editSettings(String token, SettingsRequest settingsRequest);
}
