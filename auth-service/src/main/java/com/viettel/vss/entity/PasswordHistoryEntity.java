package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "password_history")
public class PasswordHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "user_id")
    private Long userId;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;
}
