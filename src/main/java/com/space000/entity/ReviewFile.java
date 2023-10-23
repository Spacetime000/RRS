package com.space000.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Setter @Getter
public class ReviewFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_file_id")
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String oriFileName;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public void setReview(Review review) {
        this.review = review;

        if (!review.getReviewFileList().contains(this)) {
            review.addReviewFile(this);
        }
    }

}
