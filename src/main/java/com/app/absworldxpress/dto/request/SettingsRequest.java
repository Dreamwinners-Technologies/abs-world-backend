package com.app.absworldxpress.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingsRequest {
    private String shortDescription;
    private String description;

    private String facebookPage;
    private String youtubeChannel;
    private String telegramLink;
    private String whatsapp;
    private String imo;

    private Boolean isUnderMaintenance;
    private String maintenanceMsg;
    private Boolean isServerDown;
    private String serverDownMsg;
}
