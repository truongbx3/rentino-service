package com.viettel.vss.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.viettel.vss.base.BaseEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "attach_file")
public class AttachFile extends BaseEntity{
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;



    @Basic
    @Column(name = "file_name")
    private String fileName;

    @Basic
    @Column(name = "path_name")
    private String pathName;

    @Basic
    @Column(name = "system")
    private String system;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
