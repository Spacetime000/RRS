package com.space000.repository;

import com.space000.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAll(Pageable pageable);

    Page<Review> findAllByOrderByCreateTimeDesc(Pageable pageable);

    long count();

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.shop.id = :shopId")
    Double getAvgRating(@Param("shopId") Long shopId);

}
