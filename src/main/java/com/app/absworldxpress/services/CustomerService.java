package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.response.CustomerListResponse;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<ApiResponse<CustomerListResponse>> getUsersList(String token, String customerName, String customerPhoneNo, String createdBy, String sortBy, Sort.Direction orderBy, int pageSize, int pageNo);
}
