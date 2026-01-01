package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_service_registration")
public class UserServiceRegistrationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id")
    private Long id;
    
    @Basic
    @Column(name = "service_registration_id")
    private Long serviceRegistrationId;
    
    @Basic
    @Column(name = "user_id")
    private Long userId;
    
    @Basic
    @Column(name = "created_date")
    private Date createdDate;
    
    @Basic
    @Column(name = "created_by")
    private String createdBy;
    
    @Basic
    @Column(name = "updated_date")
    private Date updatedDate;
    
    @Basic
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Basic
    @Column(name = "is_deleted")
    private int isDeleted;
    
    @Basic
    @Column(name = "is_active")
    private int isActive;
}