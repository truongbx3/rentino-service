package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class TokensEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "access_token")
    private String accessToken;

    @Basic
    @Column(name = "phone")
    private String phone;

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private Date createdDate;

        @Column(name = "updated_date")
    @LastModifiedDate
    private Date updatedDate;
}
