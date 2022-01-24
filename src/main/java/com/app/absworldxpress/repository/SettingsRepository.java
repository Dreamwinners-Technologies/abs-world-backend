package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.SettingsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<SettingsModel,Long> {
    boolean existsBySettingsId(Long settingsId);
    SettingsModel getBySettingsId(Long settingsId);
    SettingsModel findBySettingsId(Long settingsId);

}
