package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "updated_date")
    @LastModifiedDate
    private Date updatedDate;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

//    @Basic
//	@Column(name = "is_deleted")
//	private int isDeleted;
}
