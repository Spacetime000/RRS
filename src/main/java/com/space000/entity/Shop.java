package com.space000.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Shop {

    @Id
    @Column(name = "shop_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String roadName;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    public void addReview(Review review) {
        this.reviewList.add(review);

        if (review.getShop() != this) {
            review.setShop(this);
        }
    }
}
