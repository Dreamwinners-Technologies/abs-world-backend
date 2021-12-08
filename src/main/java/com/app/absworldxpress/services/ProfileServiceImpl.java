package com.app.absworldxpress.services;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.EditProfileRequest;
import com.app.absworldxpress.dto.response.UserProfileResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.jwt.security.jwt.JwtProvider;
import com.app.absworldxpress.util.ImageUtilService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service

public class ProfileServiceImpl implements ProfileService {

    private UserRepository userRepository;

    JwtProvider jwtProvider;

    @Override
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(String token) {

        String username = jwtProvider.getUserNameFromJwt(token);
        System.out.println(username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()){
            User user = userOptional.get();

            UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .phoneNo(user.getPhoneNo())
                    .deliveryAddress(user.getDeliveryAddress())
                    .profileImage(user.getProfileImage())
                    .build();

            return new ResponseEntity<>(new ApiResponse<>(200,"User Profile Found",userProfileResponse), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse<>(404,"User not Found",null), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<ApiMessageResponse> editProfile(String token, EditProfileRequest editProfileRequest) {
        String username = jwtProvider.getUserNameFromJwt(token);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()){
            User user = userOptional.get();

            if (!user.getEmail().equalsIgnoreCase(editProfileRequest.getEmail())  && userRepository.existsByEmail(editProfileRequest.getEmail())){
                return new ResponseEntity<>(new ApiMessageResponse(403,"Email Already Exist"), HttpStatus.BAD_REQUEST);
            }
            user.setFullName(editProfileRequest.getFullName());

            user.setEmail(editProfileRequest.getEmail());

            user.setPhoneNo(editProfileRequest.getPhoneNo());
            user.setDeliveryAddress(editProfileRequest.getDeliveryAddress());

            userRepository.save(user);

            return new ResponseEntity<>(new ApiMessageResponse(200,"User Profile Updated"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(404,"User not Found"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<ApiMessageResponse> uploadProfileImage(String token, MultipartFile aFile) {
        String username = jwtProvider.getUserNameFromJwt(token);
        System.out.println(username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()){
            User user = userOptional.get();

            MultipartFile[] multipartFiles = new MultipartFile[1];
            multipartFiles[0] = aFile;
            List<String> profileImageLinks = new ArrayList<>();

            try {
                profileImageLinks = ImageUtilService.uploadImage(multipartFiles);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
            }

            user.setProfileImage(profileImageLinks.get(0));
            userRepository.save(user);

            return new ResponseEntity<>(new ApiMessageResponse(200,"Profile Image Uploaded"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(404,"User not Found"), HttpStatus.NOT_FOUND);
        }
    }
}
