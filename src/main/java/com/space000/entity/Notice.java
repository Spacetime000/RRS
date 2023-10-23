package com.space000.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter @Setter
@ToString
public class Notice extends BaseEntity {

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeFile> noticeFiles = new ArrayList<>();

    public void addNoticeFile(NoticeFile noticeFile) {
        this.noticeFiles.add(noticeFile);

        if (noticeFile.getNotice() != this) {
            noticeFile.setNotice(this);
        }
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
