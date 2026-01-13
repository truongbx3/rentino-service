package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "device_question")
public class DeviceQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "question")
    private String question;

    @Basic
    @Column(name = "code")
    private String code;


    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "order")
    private Integer order;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<DeviceQuestionAnswer> answers = new ArrayList<>();



//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
