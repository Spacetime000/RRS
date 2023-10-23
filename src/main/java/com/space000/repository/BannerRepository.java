package com.space000.repository;

import com.space000.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom {
}
