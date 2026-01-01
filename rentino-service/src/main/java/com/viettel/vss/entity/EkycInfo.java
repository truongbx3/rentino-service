package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "ekyc_info")
public class EkycInfo extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "id_card", length = 255)
    private String idCard;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "gender", length = 255)
    private String gender;

    @Column(name = "nationality", length = 255)
    private String nationality;

    @Column(name = "hometown", length = 255)
    private String hometown;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "identification_sign", length = 255)
    private String identificationSign;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "issued_at", length = 255)
    private String issuedAt;

    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
