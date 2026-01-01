package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "device_info")
public class DeviceInfo extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;


    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "device_name")
    private String deviceName;

    @Basic
    @Column(name = "device_code")
    private String deviceCode;

    @Basic
    @Column(name = "model")
    private String model;

    @Basic
    @Column(name = "os_version")
    private String osVersion;

    @Basic
    @Column(name = "total_ram")
    private String totalRam;

    @Basic
    @Column(name = "storage")
    private String storage;

    @Basic
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "updated_date")
    @LastModifiedDate
    private Date updatedDate;


    @Column(name = "transaction_id")
    private String transactionId;


    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

    @Column(name = "front_check")
    private String frontCheck;

    @Column(name = "back_check")
    private String backCheck;

    @Column(name = "summary")
    private String summary;

    @Column(name = "final_summary")
    private String finalSummary ;

    @Column(name = "function_check")
    private String functionCheck;

    @Column(name = "addition_check")
    private String additionCheck;

    @Column(name = "price")
    private BigDecimal price;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
