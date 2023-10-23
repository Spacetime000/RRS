package com.space000.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Review extends BaseEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private Byte rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewFile> reviewFileList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public void setMember(Member member) {
        this.member = member;

        if (!member.getReviewList().contains(this)) {
            member.addReview(this);
        }
    }

    public void addReviewFile(ReviewFile reviewFile) {
        this.reviewFileList.add(reviewFile);

        if (reviewFile.getReview() != this) {
            reviewFile.setReview(this);
        }
    }

    public void setShop(Shop shop) {
        this.shop = shop;

        if (!shop.getReviewList().contains(this)) {
            shop.addReview(this);
        }
    }

}
