package com.example.casestudy.repository;

import com.example.casestudy.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository  extends JpaRepository<Banner, Integer> {
}
