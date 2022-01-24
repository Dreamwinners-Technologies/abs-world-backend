package com.app.absworldxpress.model;

import jdk.jfr.Description;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SettingsModel {
    @Id
    private Long settingsId;

    private String logo;
    private String shortDescription;
    private String description;

    private String facebookPage;
    private String youtubeChannel;
    private String telegramLink;

    private Boolean isUnderMaintenance;
    private String maintenanceMsg;
    private Boolean isServerDown;
    private String serverDownMsg;
}
