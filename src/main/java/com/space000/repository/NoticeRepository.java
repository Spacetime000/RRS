package com.space000.repository;

import com.space000.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByOrderByCreateTimeDesc();
}
