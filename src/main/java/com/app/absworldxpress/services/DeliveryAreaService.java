package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.AddDeliveryAreaRequest;
import com.app.absworldxpress.model.DeliveryAreaModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeliveryAreaService {
    ResponseEntity<ApiResponse<List<DeliveryAreaModel>>> getDeliveryArea(String deliveryAreaName, String district);

    ResponseEntity<ApiMessageResponse> addDeliveryArea(String token, AddDeliveryAreaRequest addDeliveryAreaRequest);

    ResponseEntity<ApiMessageResponse> deleteDeliveryArea(String token, String deliveryAreaId);
}
