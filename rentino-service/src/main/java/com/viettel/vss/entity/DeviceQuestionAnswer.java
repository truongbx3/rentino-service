package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "device_question_answer")
public class DeviceQuestionAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Basic
    @Column(name = "answer")
    private String answer;

    @Basic
    @Column(name = "value")
    private String value;

    @Column(name = "question_code") // ✅ thêm cột FK dạng string
    private String questionCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_code", referencedColumnName = "code",
            insertable = false, updatable = false)
    private DeviceQuestion question;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
