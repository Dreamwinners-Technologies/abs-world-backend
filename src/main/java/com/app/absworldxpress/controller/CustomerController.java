package com.app.absworldxpress.controller;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.response.CustomerListResponse;
import com.app.absworldxpress.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/customers/")
public class CustomerController {
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerListResponse>> getUsersList(@RequestHeader(name = "Authorization") String token,
                                                                          @RequestParam(required = false) String customerName,
                                                                          String customerPhoneNo, String createdBy,
                                                                          @RequestParam(defaultValue = "creationTime") String sortBy,
                                                                          @RequestParam(defaultValue = "ASC") Sort.Direction orderBy,
                                                                          @RequestParam(defaultValue = "20") int pageSize,
                                                                          @RequestParam(defaultValue = "0") int pageNo){
        return customerService.getUsersList(token,customerName,customerPhoneNo,createdBy,sortBy,orderBy,pageSize,pageNo);
    }
}
