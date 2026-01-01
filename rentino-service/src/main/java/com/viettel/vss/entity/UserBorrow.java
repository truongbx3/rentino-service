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
@Table(name = "user_borrow")
public class UserBorrow extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "borrow_from_date")
    private Date borrowFromDate;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

    @Column(name = "borrow_to_date")
    private Date borrowToDate;

    @Column(name = "total_date")
    private Long totalDate;

    @Column(name = "fixed_fee")
    private BigDecimal fixedFee;

    @Basic
    @Column(name = "transaction")
    private String transaction;

    @Column(name = "daily_fee")
    private BigDecimal dailyFee;

    @Column(name = "interest_fee")
    private BigDecimal interestFee;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "value_borrow")
    private BigDecimal valueBorrow;

    @Basic
	@Column(name = "is_deleted")
	private int isDeleted;
}
