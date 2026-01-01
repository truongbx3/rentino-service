package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "versions")
public class VersionsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id")
    private Long id;
    
    @Basic
    @Column(name = "code")
    private String code;
    
    @Basic
    @Column(name = "image")
    private String image;
    

}