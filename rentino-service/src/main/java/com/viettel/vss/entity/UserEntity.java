package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "users")
public class UserEntity extends BaseEntity {

    
    @Basic
    @Column(name = "first_name")
    private String firstName;
    
    @Basic
    @Column(name = "last_name")
    private String lastName;
    
    @Basic
    @Column(name = "email")
    private String email;
    
    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "salt")
    private String salt;


    @Basic
    @Column(name = "is_first_login")
    private Boolean isFirstLogin = true;

}