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
@Table(name = "Interest")
public class Interest extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    @Basic
    @Column(name = "type")
    private String type;

    @Column(name = "from_date")
    @LastModifiedDate
    private Date fromDate;

    @CreatedDate
    @Column(name = "to_date")
    private Date toDate;


    @Column(name = "price")
    private BigDecimal price;

    @Basic
	@Column(name = "is_deleted")
	private int isDeleted;

    @Basic
	@Column(name = "is_fixed")
	private int isFixed;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "max_borrow")
    private BigDecimal maxBorrow;

}
