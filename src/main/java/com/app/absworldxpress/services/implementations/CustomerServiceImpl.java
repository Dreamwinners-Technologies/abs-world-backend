package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.response.CustomerListResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.services.CustomerService;
import com.app.absworldxpress.util.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private UserRepository userRepository;
    private AuthService authService;
    private UtilService utilService;

    @Override
    public ResponseEntity<ApiResponse<CustomerListResponse>> getUsersList(String token, String customerName, String customerPhoneNo, String createdBy, String sortBy, Sort.Direction orderBy, int pageSize, int pageNo) {
        User example = User.builder()
                .fullName(customerName)
                .phoneNo(customerPhoneNo)
                .createdBy(createdBy)
                .build();

        Pageable pageable;
        Sort sort = Sort.by(orderBy,sortBy);
        pageable = PageRequest.of(pageNo, pageSize,sort);

        Page<User> userPage;

        if(authService.isThisUser("ADMIN",token)){
            userPage = userRepository.findAll(Example.of(example),pageable);

            CustomerListResponse customerListResponse = new CustomerListResponse(userPage.getSize(),userPage.getNumber(),userPage.getTotalPages(),userPage.isLast(),userPage.getTotalElements(),userPage.getTotalPages(),userPage.getContent());

            if (userPage.isEmpty()){
                return new ResponseEntity<>(new ApiResponse<>(200,"No Customer Found!",null),HttpStatus.OK);
            }
            else return new ResponseEntity<>(new ApiResponse<>(200,"Customer List Found!",customerListResponse),HttpStatus.OK);
        }
        else return new ResponseEntity<>(new ApiResponse<>(400,"You Have No permission to get customers",null), HttpStatus.BAD_REQUEST);
    }
}
