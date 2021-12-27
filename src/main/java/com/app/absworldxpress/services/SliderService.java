package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.SliderRequest;
import com.app.absworldxpress.model.SliderModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SliderService {
    ResponseEntity<ApiResponse<SliderModel>> addSlider(String token, SliderRequest sliderRequest);

    ResponseEntity<ApiResponse<List<SliderModel>>> getSliderList();

    ResponseEntity<ApiMessageResponse> uploadSliderImage(String token, MultipartFile aFile, Long sliderId);

    ResponseEntity<ApiMessageResponse> deleteSlider(String token, Long sliderId);
}
