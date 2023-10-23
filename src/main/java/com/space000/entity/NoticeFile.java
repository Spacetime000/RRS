package com.space000.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "notice_file")
@Getter @Setter
public class NoticeFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notice_file_id")
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String oriFileName;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    public void setNotice(Notice notice) {
        this.notice = notice;

        if (!notice.getNoticeFiles().contains(this)) {
            notice.addNoticeFile(this);
        }
    }

}
