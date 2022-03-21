package com.app.absworldxpress.jwt.services.Implementation;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.jwt.services.ForgetPasswordService;
import com.app.absworldxpress.util.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ForgetPasswordImpl implements ForgetPasswordService {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private EmailSenderService emailSenderService;
    @Override
    public ResponseEntity<ApiMessageResponse> changePassword(Integer otp, String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            User user = userOptional.get();

            if (user.getGeneratedOTP() == (int) otp){
                int newOtp = (int) (Math.random()*9000)+1000;

                user.setPassword(encoder.encode(newPassword));
                user.setGeneratedOTP(newOtp);
                userRepository.save(user);

                return new ResponseEntity<>(new ApiMessageResponse(200,"Password Changed!"), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ApiMessageResponse(400,"OTP Not Matched"), HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(404, "User Not Exist"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<ApiMessageResponse> generateOTP(String email) {
        int otp = (int) (Math.random()*9000)+1000;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setGeneratedOTP(otp);

            userRepository.save(user);

            emailSenderService.sendEmail(email,"Dear "+user.getFullName()+", Your OTP is "+String.valueOf(otp),"OTP From ABS World Express");

            return new ResponseEntity<>(new ApiMessageResponse(200,"OTP Generated : "), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiMessageResponse(404, "User Not Exist"), HttpStatus.NOT_FOUND);
        }
    }
}
