package com.space000.repository;

import com.space000.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopRepositoryCustom {

    @Query("SELECT DISTINCT(s.category) FROM Shop s ORDER BY s.category")
    List<String> getCategory();
}
