package com.angular.Image.Repository;

import com.angular.Image.Models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    @Query("from ImageModel m where m.name = :name")
    ImageModel findByName(String name);
}