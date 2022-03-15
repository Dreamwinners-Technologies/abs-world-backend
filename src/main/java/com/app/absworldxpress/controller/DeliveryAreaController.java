package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.AddDeliveryAreaRequest;
import com.app.absworldxpress.model.DeliveryAreaModel;
import com.app.absworldxpress.services.DeliveryAreaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/delivery-area")
@AllArgsConstructor
public class DeliveryAreaController {
    private final DeliveryAreaService deliveryAreaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryAreaModel>>> getDeliveryArea(@RequestParam(required = false) String deliveryAreaName,
                                                                                @RequestParam(required = false) String district,
                                                                                @RequestParam(required = false) String country){
        return deliveryAreaService.getDeliveryArea(deliveryAreaName,district, country);
    }
    @PostMapping
    public ResponseEntity<ApiMessageResponse> addDeliveryArea(@RequestHeader(name = "Authorization") String token,
                                                              @RequestBody AddDeliveryAreaRequest addDeliveryAreaRequest){
        return deliveryAreaService.addDeliveryArea(token,addDeliveryAreaRequest);
    }
    @DeleteMapping("/{deliveryAreaId}")
    public ResponseEntity<ApiMessageResponse> deleteDeliveryArea(@RequestHeader(name = "Authorization") String token, @PathVariable String deliveryAreaId){
        return deliveryAreaService.deleteDeliveryArea(token,deliveryAreaId);
    }
}
