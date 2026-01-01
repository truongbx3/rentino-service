package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_ekyc")
public class UserEkyc extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "transaction_id", length = 255)
    private String transactionId;


    @Column(name = "ocr_front", length = 255)
    private String ocrFront;

    @Column(name = "ocr_back", length = 255)
    private String ocrBack;

    @Column(name = "portrait_left", length = 255)
    private String portraitLeft;

    @Column(name = "portrait_mid")
    private String portraitMid;

    @Column(name = "portrait_right", length = 255)
    private String portraitRight;

    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "status")
    private String status;

    @Column(name = "message")
    private String message;


    @Column(name = "step")
    private String step;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
