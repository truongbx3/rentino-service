package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "device_price")
public class DevicePrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "device_code")
    private String deviceCode;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "model")
    private String model;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "total_ram")
    private String totalRam;

    @Column(name = "storage")
    private String storage;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "bonus")
    private BigDecimal bonus;


//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
