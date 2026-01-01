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
@Table(name = "system_config")
public class SystemConfig extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "is_deleted")
	private int isDeleted;

    @Basic
	@Column(name = "type")
	private int type;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "value")
    private String value;
    

}
