package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "system_tokens")
public class SystemToken extends BaseEntity {

	@Basic
	@Column(name = "token")
	private String token;

	@Basic
	@Column(name = "expired_at")
	private Date expiredAt;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_auth_id", nullable = false)
    private SystemAuth systemAuth;
}
