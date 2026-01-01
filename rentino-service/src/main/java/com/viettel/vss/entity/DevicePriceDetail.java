package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "device_price_detail")
public class DevicePriceDetail extends BaseEntity {

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

    @Column(name = "price")
    private BigDecimal price;


//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
