package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.SliderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SliderRepository extends JpaRepository<SliderModel,Long> {
}
