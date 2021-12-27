package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.SliderRequest;
import com.app.absworldxpress.dto.response.CategoryImageResponse;
import com.app.absworldxpress.jwt.security.jwt.JwtProvider;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.model.SliderModel;
import com.app.absworldxpress.repository.SliderRepository;
import com.app.absworldxpress.util.ImageUtilService;
import com.app.absworldxpress.util.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SliderServiceImpl implements SliderService{

    private final SliderRepository sliderRepository;
    private final JwtProvider jwtProvider;
    private final UtilService utilService;
    private final AuthService authService;

    @Override
    public ResponseEntity<ApiResponse<SliderModel>> addSlider(String token, SliderRequest sliderRequest) {
        if (authService.isThisUser("ADMIN", token)){
            SliderModel sliderModel = new SliderModel();
            sliderModel.setTitle(sliderRequest.getTitle());
            sliderModel.setLinkedId(sliderRequest.getLinkedId());

            sliderRepository.save(sliderModel);
            return new ResponseEntity<>(
                    new ApiResponse<>(201, "Slider Added Successfully!",sliderModel)
            , HttpStatus.CREATED);
        }
        else
        return new ResponseEntity<>(new ApiResponse<>(401,"You Have no permission to add slider",null), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiResponse<List<SliderModel>>> getSliderList() {
        List<SliderModel> sliderModelList = new ArrayList<SliderModel>();
        sliderModelList = sliderRepository.findAll();
        if (!sliderModelList.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(200,"Slider Found",sliderModelList), HttpStatus.OK);
        }
        else
        return new ResponseEntity<>(new ApiResponse<>(200,"No Slider Found",null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> uploadSliderImage(String token, MultipartFile aFile, Long sliderId) {
        if (authService.isThisUser("ADMIN", token)){
            if (sliderRepository.existsById(sliderId)){
                SliderModel sliderModel = sliderRepository.getById(sliderId);

                MultipartFile[] multipartFiles = new MultipartFile[1];
                multipartFiles[0] = aFile;
                List<String> sliderImageLinks = new ArrayList<>();

                try {
                    sliderImageLinks = ImageUtilService.uploadImage(multipartFiles);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
                }

                sliderModel.setImageURL(sliderImageLinks.get(0));
                sliderRepository.save(sliderModel);

                CategoryImageResponse categoryImageResponse = new CategoryImageResponse();
                categoryImageResponse.setCatImage(sliderImageLinks.get(0));
                return new ResponseEntity<>(new ApiMessageResponse(200,"SliderImage Uploaded Successfully"),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ApiMessageResponse(200,"Slider Not Found"),HttpStatus.OK);
            }
        }
        else
            return new ResponseEntity<>(new ApiMessageResponse(401,"You have no permission"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> deleteSlider(String token, Long sliderId) {
        if (authService.isThisUser("ADMIN", token)){
            if (sliderRepository.existsById(sliderId)){

                sliderRepository.deleteById(sliderId);

                return new ResponseEntity<>(new ApiMessageResponse(200,"Slider Deleted Successfully"),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ApiMessageResponse(200,"Slider Not Found"),HttpStatus.OK);
            }
        }
        else
            return new ResponseEntity<>(new ApiMessageResponse(401,"You have no permission"),HttpStatus.BAD_REQUEST);
    }

}
