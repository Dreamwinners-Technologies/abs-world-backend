package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.SliderRequest;
import com.app.absworldxpress.dto.response.CategoryImageResponse;
import com.app.absworldxpress.model.SliderModel;
import com.app.absworldxpress.services.SliderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/slider/")
public class SliderController {
    private final SliderService sliderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SliderModel>>> getSliderList(){
        return sliderService.getSliderList();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SliderModel>> addSlider(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody SliderRequest sliderRequest){
        return sliderService.addSlider(token,sliderRequest);
    }

    @DeleteMapping("/{sliderId}")
    public ResponseEntity<ApiMessageResponse> deleteSlider(@RequestHeader(name = "Authorization") String token,
                                                                @PathVariable Long sliderId) {
        return sliderService.deleteSlider(token,sliderId);
    }

    @PostMapping("/image/{sliderId}")
    public ResponseEntity<ApiMessageResponse> uploadSliderImage(@RequestHeader(name = "Authorization") String token,
                                                                                  @RequestParam(value = "image", required = true) MultipartFile aFile,
                                                                                  @PathVariable Long sliderId) {
        return sliderService.uploadSliderImage(token,aFile,sliderId);
    }

}
