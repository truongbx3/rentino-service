package com.viettel.vss.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Immutable
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "emp_code")
    private String empCode;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "full_name")
    private String fullName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "company")
    private String company;

    @Basic
    @Column(name = "company_code")
    private String companyCode;

    @Basic
    @Column(name = "failed_attempts")
    private Integer failedAttempts; // Số lần đăng nhập thất bại

    @Basic
    @Column(name = "account_locked")
    private Boolean accountLocked; // Tài khoản có bị khóa không

    @Basic
    @Column(name = "FIRST_FAILED_LOGINTIME")
    private Date firstFailedLoginTime; // Thời gian đăng nhập thất bại lần đầu

    @Basic
    @Column(name = "LAST_FAILED_LOGINTIME")
    private Date lastFailedLoginTime; // Thời gian đăng nhập thất bại mới nhất

    @Basic
    @Column(name = "status_id")
    private Long statusId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_group_id")
    )
    @JsonManagedReference
    private List<RoleGroup> roleGroups;


    @Basic
    @Column(name = "salt")
    private String salt;

    @Basic
    @Column(name = "is_first_login")
    private Boolean isFirstLogin = true;

    @Basic
    @Column(name = "description")
    private String description;

}
