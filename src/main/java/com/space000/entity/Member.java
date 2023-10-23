package com.space000.entity;

import com.space000.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;
    private String sex;
    private LocalDate birth;
    private String tel;
    private Integer zoneCode;
    private String roadName;
    private String detailedAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberImg memberImg;

    public void setMemberImg(MemberImg memberImg) {
        this.memberImg = memberImg;

        if (memberImg.getMember() != this) {
            memberImg.setMember(this);
        }
    }

    public void addReview(Review review) {
        this.reviewList.add(review);

        if (review.getMember() != this) {
            review.setMember(this);
        }
    }
}
