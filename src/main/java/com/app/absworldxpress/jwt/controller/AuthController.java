package com.app.absworldxpress.jwt.controller;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.jwt.dto.request.LoginForm;
import com.app.absworldxpress.jwt.dto.request.SignUpForm;
import com.app.absworldxpress.jwt.dto.request.JoiningForm;
import com.app.absworldxpress.jwt.dto.response.LoggedUserDetailsResponse;
import com.app.absworldxpress.jwt.model.User;
import com.app.absworldxpress.jwt.services.ForgetPasswordService;
import com.app.absworldxpress.jwt.services.SignUpAndSignInService;
import javassist.bytecode.DuplicateMemberException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private SignUpAndSignInService signUpAndSignInService;
    private ForgetPasswordService forgetPasswordService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        return ResponseEntity.ok(signUpAndSignInService.signIn(loginRequest));
    }


    @PostMapping("/new-joining")
    public Object registerUser(@RequestBody JoiningForm signUpRequest) throws DuplicateMemberException {
        return signUpAndSignInService.newJoining(signUpRequest);
    }

    @PostMapping("/signup")
    public Object ecommerceSignup(@RequestBody SignUpForm signUpRequest) throws DuplicateMemberException {
        return signUpAndSignInService.ecommerceSignup(signUpRequest);
    }

    @PutMapping("/generateOTP")
    public ResponseEntity<ApiMessageResponse> generateOTP(@RequestParam String email){
        return forgetPasswordService.generateOTP(email);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ApiMessageResponse> changePassword(@RequestParam Integer otp, @RequestParam String email,
                                                          @RequestParam String newPassword){
        return forgetPasswordService.changePassword(otp,email,newPassword);
    }

    @GetMapping("/serverCheck")
    public String getServerStatStatus() {
        return "The Server is Running";
    }

//    @GetMapping("/sendMail")
//    public String sendMail()throws MessagingException, IOException {
//        return forgetPasswordService.sendMail();
//    }

    @GetMapping("/user-info")
    public LoggedUserDetailsResponse getDashboard(Authentication authentication) {

        return signUpAndSignInService.getLoggedUserDetails(authentication);
    }

    @GetMapping("user/createdBy/{id}")
    public ResponseEntity<ApiResponse<List<User>>> getTeamMembers(@RequestParam String id){
        return signUpAndSignInService.getTeamMembers(id);
    }

}
