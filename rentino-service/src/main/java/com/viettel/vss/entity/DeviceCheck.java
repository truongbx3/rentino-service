package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "device_check")
public class DeviceCheck extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    @Basic
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "transaction_id")
    private String transactionId;

    @Basic
    @Column(name = "item")
    private String item;

    @Column(name = "value")
    private String value;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
