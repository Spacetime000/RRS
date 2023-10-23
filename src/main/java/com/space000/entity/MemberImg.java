package com.space000.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
public class MemberImg {

    @Id
    @Column(name = "member_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String oriFileName;

    @Column(nullable = false)
    private String fileUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;

        if (member.getMemberImg() != this) {
            member.setMemberImg(this);
        }
    }

    public static MemberImg createMemberImg() {
        MemberImg memberImg = new MemberImg();
        memberImg.setFileName("default");
        memberImg.setOriFileName("default");
        memberImg.setFileUrl("/img/person-circle.svg");
        return memberImg;
    }

}
