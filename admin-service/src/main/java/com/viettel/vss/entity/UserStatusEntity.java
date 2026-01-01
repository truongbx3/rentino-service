package com.viettel.vss.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_status")
@Data
public class UserStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
