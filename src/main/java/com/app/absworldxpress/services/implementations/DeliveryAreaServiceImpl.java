package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.BasicTableInfo;
import com.app.absworldxpress.dto.request.AddDeliveryAreaRequest;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.model.DeliveryAreaModel;
import com.app.absworldxpress.repository.DeliveryAreaRepository;
import com.app.absworldxpress.services.DeliveryAreaService;
import com.app.absworldxpress.util.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeliveryAreaServiceImpl implements DeliveryAreaService {

    private DeliveryAreaRepository deliveryAreaRepository;
    private AuthService authService;
    private UtilService utilService;

    @Override
    public ResponseEntity<ApiResponse<List<DeliveryAreaModel>>> getDeliveryArea(String deliveryAreaName, String district, String country) {
        DeliveryAreaModel deliveryAreaModel = DeliveryAreaModel.builder()
                .deliveryAreaName(deliveryAreaName)
                .district(district)
                .country(country)
                .build();
        Sort sort = Sort.by("deliveryAreaName");

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withMatcher("deliveryAreaName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("district", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("country", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());


        List<DeliveryAreaModel> deliveryAreaModelList = deliveryAreaRepository.findAll(Example.of(deliveryAreaModel,matcher),sort);

        if (deliveryAreaModelList.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(200,"No Delivery Area Found!",deliveryAreaModelList),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(200,"Delivery Area Found",deliveryAreaModelList),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiMessageResponse> addDeliveryArea(String token, AddDeliveryAreaRequest addDeliveryAreaRequest) {
        if (authService.isThisUser("ADMIN",token)){
            DeliveryAreaModel deliveryAreaModel = new DeliveryAreaModel();
            BasicTableInfo basicTableInfo = utilService.generateBasicTableInfo(addDeliveryAreaRequest.getDeliveryAreaName(),token);
            deliveryAreaModel.setDeliveryAreaId(basicTableInfo.getId());
            deliveryAreaModel.setDeliveryAreaName(addDeliveryAreaRequest.getDeliveryAreaName());
            deliveryAreaModel.setCountry(addDeliveryAreaRequest.getCountry());
            deliveryAreaModel.setDistrict(addDeliveryAreaRequest.getDistrict());
            deliveryAreaModel.setDivision(addDeliveryAreaRequest.getDivision());
            deliveryAreaModel.setDeliveryCharge(addDeliveryAreaRequest.getDeliveryCharge());

            deliveryAreaRepository.save(deliveryAreaModel);
            return new ResponseEntity<>(new ApiMessageResponse(201,"Delivery Area Added"),HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(401,"Your have no Permission"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiMessageResponse> deleteDeliveryArea(String token, String deliveryAreaId) {
        if (authService.isThisUser("ADMIN",token)){
            Optional<DeliveryAreaModel> deliveryAreaModelOptional = deliveryAreaRepository.findById(deliveryAreaId);
            if (deliveryAreaModelOptional.isPresent()){

                deliveryAreaRepository.deleteById(deliveryAreaId);
                return new ResponseEntity<>(new ApiMessageResponse(201,"Delivery Area Added"),HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>(new ApiMessageResponse(404,"Delivery Area Not Found"), HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(401,"Your have no Permission"), HttpStatus.BAD_REQUEST);
        }
    }
}
