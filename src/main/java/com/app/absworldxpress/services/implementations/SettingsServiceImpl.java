package com.app.absworldxpress.services.implementations;

import com.app.absworldxpress.dto.ApiMessageResponse;
import com.app.absworldxpress.dto.ApiResponse;
import com.app.absworldxpress.dto.request.SettingsRequest;
import com.app.absworldxpress.jwt.services.AuthService;
import com.app.absworldxpress.model.SettingsModel;
import com.app.absworldxpress.repository.SettingsRepository;
import com.app.absworldxpress.services.SettingsService;
import com.app.absworldxpress.util.ImageUtilService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class SettingsServiceImpl implements SettingsService {

    private SettingsRepository settingsRepository;
    private AuthService authService;

    @Override
    public ResponseEntity<ApiMessageResponse> initSettings(String token) {
       if (authService.isThisUser("ADMIN", token)){
           SettingsModel settingsModel = new SettingsModel();
           settingsModel.setSettingsId(2022L);
           settingsModel.setIsUnderMaintenance(false);
           settingsModel.setIsServerDown(false);
           settingsRepository.save(settingsModel);

           return new ResponseEntity<>(new ApiMessageResponse(200,"Setting Initilized Successful!"), HttpStatus.OK);
       }
       else {
           return new ResponseEntity<>(new ApiMessageResponse(401,"You Have no permission to init settings"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiMessageResponse> uploadLogo(String token, MultipartFile aFile) {
        if (authService.isThisUser("ADMIN", token)) {
            if (settingsRepository.existsBySettingsId(2022L)) {
                SettingsModel settingsModel = settingsRepository.getBySettingsId(2022L);

                MultipartFile[] multipartFiles = new MultipartFile[1];
                multipartFiles[0] = aFile;
                List<String> imageLinks = new ArrayList<>();

                try {
                    imageLinks = ImageUtilService.uploadImage(multipartFiles);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
                }

                settingsModel.setLogo(imageLinks.get(0));
                settingsRepository.save(settingsModel);

                return
                        new ResponseEntity<>(new ApiMessageResponse(200, "Logo Uploaded Successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiMessageResponse(404, "Settings is not Initialized"), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(new ApiMessageResponse(401, "You Have no permission"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<SettingsModel>> getSettings() {
            if (settingsRepository.existsBySettingsId(2022L)) {
                SettingsModel settingsModel = settingsRepository.getBySettingsId(2022L);
                return new ResponseEntity<>(new ApiResponse<>(200, "Settings Found", settingsModel), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ApiResponse<>(404, "Settings Not Found", null), HttpStatus.NOT_FOUND);
            }
    }

    @Override
    public ResponseEntity<ApiResponse<SettingsModel>> editSettings(String token, SettingsRequest settingsRequest) {
        if (authService.isThisUser("ADMIN", token)) {
            if (settingsRepository.existsBySettingsId(2022L)) {
                SettingsModel settingsModel = settingsRepository.getBySettingsId(2022L);

                settingsModel.setShortDescription(settingsRequest.getShortDescription());
                settingsModel.setDescription(settingsRequest.getDescription());
                settingsModel.setFacebookPage(settingsRequest.getFacebookPage());
                settingsModel.setYoutubeChannel(settingsRequest.getYoutubeChannel());
                settingsModel.setTelegramLink(settingsRequest.getTelegramLink());
                settingsModel.setIsUnderMaintenance(settingsRequest.getIsUnderMaintenance());
                settingsModel.setMaintenanceMsg(settingsRequest.getMaintenanceMsg());
                settingsModel.setIsServerDown(settingsRequest.getIsServerDown());
                settingsModel.setServerDownMsg(settingsRequest.getServerDownMsg());

                settingsRepository.save(settingsModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Settings Updated", settingsModel), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(404, "Settings Not Found", null), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(new ApiResponse<>(401, "Only admin can change settings", null), HttpStatus.BAD_REQUEST);
        }
    }
}
