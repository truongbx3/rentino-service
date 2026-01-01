package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "refresh_token")
    private String refreshToken;

    @Basic
    @Column(name = "exp_date")
    private Date expDate;

}
