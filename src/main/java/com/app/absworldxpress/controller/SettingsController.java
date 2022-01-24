package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.SettingsRequest;
import com.app.absworldxpress.dto.response.ProductImageResponse;
import com.app.absworldxpress.model.SettingsModel;
import com.app.absworldxpress.services.SettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/settings/")
public class SettingsController {
    private SettingsService settingsService;

    @PostMapping
    public ResponseEntity<ApiMessageResponse> initSettings(@RequestHeader(name = "Authorization") String token){
        return settingsService.initSettings(token);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<SettingsModel>> editSettings(@RequestHeader(name = "Authorization") String token,
                                                           @RequestBody SettingsRequest settingsRequest){
        return settingsService.editSettings(token,settingsRequest);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SettingsModel>> getSettings(){
        return settingsService.getSettings();
    }

    @PostMapping("/image")
    public ResponseEntity<ApiMessageResponse> uploadLogo(@RequestHeader(name = "Authorization") String token,
                                                                                @RequestParam(value = "image", required = true) MultipartFile aFile) {
        return settingsService.uploadLogo(token,aFile);
    }
}
