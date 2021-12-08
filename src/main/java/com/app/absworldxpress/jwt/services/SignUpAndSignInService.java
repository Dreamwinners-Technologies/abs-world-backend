package com.app.absworldxpress.jwt.services;

import com.app.absworldxpress.jwt.dto.request.SignUpForm;
import com.app.absworldxpress.jwt.dto.request.JoiningForm;
import com.app.absworldxpress.jwt.dto.response.JwtResponse;
import com.app.absworldxpress.jwt.dto.response.LoggedUserDetailsResponse;
import com.app.absworldxpress.jwt.dto.response.UserResponse;
import com.app.absworldxpress.jwt.repository.RoleRepository;
import com.app.absworldxpress.jwt.repository.UserRepository;
import com.app.absworldxpress.jwt.security.jwt.JwtProvider;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.jwt.dto.request.LoginForm;
import com.app.absworldxpress.jwt.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@AllArgsConstructor
@Service
public class SignUpAndSignInService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    AuthService authService;

    public Object newJoining(JoiningForm signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            //return true;
            return new JwtResponse("Email Already Exists");
        }


        User user = new User();
        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        user.setId(uuid);
        user.setFullName(signUpRequest.getFullName());
        String[] arrOfUsername = signUpRequest.getEmail().split("@", 2);
        user.setUsername(arrOfUsername[0]);
        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNo(signUpRequest.getPhoneNo());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRoles(authService.getRolesFromStringToRole(signUpRequest.getRole()));
        user.setCreatedBy(signUpRequest.getCreatedBy());
        user.setCreatedOn(signUpRequest.getCreatedOn());
        userRepository.saveAndFlush(user);
        System.out.println(1);
//        if (signUpRequest.getRole().contains("PROJECT_MANAGER")){
//            Team team = new Team();
//            UUID teamId = UUID.randomUUID();
//            String teamUuid = teamId.toString();
//            team.setId(teamUuid);
//            team.setName(signUpRequest.getFirstName()+"'s Team");
//            team.setCreatedBy(uuid);
//            teamRepository.save(team);
//        }
//        System.out.println(2);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtResponse("OK", jwt, signUpRequest.getRole());
    }


    public Object ecommerceSignup(SignUpForm signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            //return true;
            return new JwtResponse("Email Already Exists");
        }


        User user = new User();
        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        user.setId(uuid);
        user.setFullName(signUpRequest.getFullName());

        String[] arrOfUsername = signUpRequest.getEmail().split("@", 2);
        String username = arrOfUsername[0];
        Integer suffix = 0;

        if (userRepository.existsByUsername(username)){
            String name = username+"0"+suffix;
            while(userRepository.existsByUsername(name)){
                suffix++;
                name = username+"0"+suffix;
            }
            username = name;
        }
        user.setUsername(username);

        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNo(signUpRequest.getPhoneNo());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRoles(authService.getRolesFromStringToRole(signUpRequest.getRole()));

        user.setCreatedBy(arrOfUsername[0]);
//        user.setCreatedOn(signUpRequest.getCreatedOn());

        userRepository.saveAndFlush(user);
        System.out.println(1);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtResponse("OK", jwt, signUpRequest.getRole());
    }


    public JwtResponse signIn(LoginForm loginRequest) {

        Optional<User> userOptional;
        if (loginRequest.getUsername().contains("@")){
                userOptional = userRepository.findByEmail(loginRequest.getUsername());
        }
        else {
                userOptional = userRepository.findByUsername(loginRequest.getUsername());
        }

        String userName;

        if (userOptional.isPresent()) {
            userName = userOptional.get().getUsername();
        } else {
            userName = "";
            //throw new ResponseStatusException(HttpStatus.valueOf(410),"User Not Exists");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        System.out.println(jwt);
        return new JwtResponse("OK", jwt, authService.getRolesStringFromRole(userOptional.get().getRoles()));
    }

    public ResponseEntity<UserResponse> getLoggedAuthUser() {

        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser instanceof UserDetails) {
            String username = ((UserDetails) authUser).getUsername();

            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(),
                        user.getPhoneNo(), authService.getRolesStringFromRole(user.getRoles()), user.getCreatedBy(), user.getCreatedOn());

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("massage", "OK");
                return new ResponseEntity(userResponse, httpHeaders, HttpStatus.OK);


            } else {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("massage", "No User Found");
                return new ResponseEntity(new UserResponse(), httpHeaders, HttpStatus.NO_CONTENT);
            }

        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "Unauthenticated");
            return new ResponseEntity(new UserResponse(), httpHeaders, HttpStatus.UNAUTHORIZED);
        }

    }


//    public ResponseEntity changePass(PassChangeRequest passChangeRequest) {
//
//        Optional<User> userOptional = userRepository.findByUsername(getLoggedAuthUser().getBody().getUsername());
//
//        if(userOptional.isPresent()){
//            User user = userOptional.get();
//            if(encoder.matches(passChangeRequest.getOldPass(), user.getPassword())) {
//                user.setPassword(encoder.encode(passChangeRequest.getNewPass()));
//
//                userRepository.save(user);
//
//                MessageResponse messageResponse = new MessageResponse("Pass Changed Successful", 200);
//                return new ResponseEntity(messageResponse, HttpStatus.OK);
//            }
//            else {
//                MessageResponse messageResponse = new MessageResponse("Old Pass Not Matched", 400);
//                return new ResponseEntity(messageResponse, HttpStatus.BAD_REQUEST);
//            }
//        }
//        else {
//            MessageResponse messageResponse = new MessageResponse("No User Found", 204);
//            return new ResponseEntity(messageResponse, HttpStatus.NO_CONTENT);
//        }
//    }

//    public String deleteUser(String email) {
//
//        if (userRepository.findByEmail(email).isPresent()) {
//
//            userRepository.deleteById(userRepository.findByEmail(email).get().getId());
//            return "Deleted";
//        } else {
//            return "Not Found";
//        }
//
//    }
//
//    public String editProfile(EditProfile editProfile) {
//        String username = getLoggedAuthUserName();
//
//        if (!username.isEmpty()) {
//            //System.out.println(username);
//            Optional<User> userOptional = userRepository.findByUsername(username);
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//                if (!editProfile.getName().isEmpty()) {
//                    user.setName(editProfile.getName());
//                }
//                if (!editProfile.getPhoneNo().isEmpty()) {
//                    user.setPhoneNo(editProfile.getPhoneNo());
//                }
//                if (!editProfile.getNewPassword().isEmpty() && !editProfile.getCurrentPassword().isEmpty()) {
//                    if (encoder.matches(editProfile.getCurrentPassword(), userOptional.get().getPassword())) {
//
//                        user.setPassword(encoder.encode(editProfile.getNewPassword()));
//                    } else {
//                        return "Wrong Current Password";
//                    }
//                }
//
//                userRepository.save(user);
//                return "Saved Successfully";
//            } else {
//                return "User Not Found";
//            }
//
//        } else {
//            return "Unsuccessful";
//        }
//
//
//    }
//
//    public String addAreaList(AreaNameRequestsResponse areaNameRequestsResponse) {
//        for (String names : areaNameRequestsResponse.getAreaNames()) {
//            AreaNames areaNames = new AreaNames(names);
//            areaNameRepository.save(areaNames);
//        }
//        return "Saved";
//    }
//
//    public AreaNameRequestsResponse getAreaList() {
//        List<AreaNames> areaNamesOptional = areaNameRepository.findAll();
//
//        AreaNameRequestsResponse areaNameRequestsResponse = new AreaNameRequestsResponse();
//        List<String> areaNamesList = new ArrayList<>();
//        for (AreaNames areaNames : areaNamesOptional) {
//            areaNamesList.add(areaNames.getAreaName());
//        }
//        areaNameRequestsResponse.setAreaNames(areaNamesList);
//        return areaNameRequestsResponse;
//    }

    public LoggedUserDetailsResponse getLoggedUserDetails(Authentication authentication) {

        System.out.println(authentication.toString());
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        List<String> userRoleList = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            userRoleList.add(grantedAuthority.getAuthority());
        }
        LoggedUserDetailsResponse loggedUserDetailsResponse = new LoggedUserDetailsResponse();
        loggedUserDetailsResponse.setUserRole(userRoleList);
        loggedUserDetailsResponse.setIsAuthenticated(authentication.isAuthenticated());

        Optional<User> user = userRepository.findByUsername(authentication.getName());
//        Team team = teamRepository.findByCreatedBy(user.get().getId()).get();

        loggedUserDetailsResponse.setId(user.get().getId());
        loggedUserDetailsResponse.setFullName(user.get().getFullName());
        loggedUserDetailsResponse.setUsername(user.get().getUsername());
        loggedUserDetailsResponse.setEmail(user.get().getEmail());
        loggedUserDetailsResponse.setPhoneNo(user.get().getPhoneNo());
//        loggedUserDetailsResponse.setProjects(user.get().getProjects());
//        loggedUserDetailsResponse.setTeamDetails(team);
        return loggedUserDetailsResponse;
    }


    public ResponseEntity<ApiResponse<List<User>>> getTeamMembers(String id) {
        List<User> users = userRepository.findByCreatedBy(id);
        return new ResponseEntity<>(new ApiResponse<>(200,"Data Found",users),HttpStatus.OK);
    }
}
