package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "otp")
public class OtpEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id")
    private Long id;
    
    @Basic
    @Column(name = "user_id")
    private Long userId;
    
    @Basic
    @Column(name = "otp")
    private String otp;
    
    @Basic
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

    @Column( name = "status")
    private String status;

    @Column( name = "response")
    private String response;
}