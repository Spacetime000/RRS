package com.space000.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Banner {

    @Id
    @Column(name = "banner_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String oriFileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String state;

}
