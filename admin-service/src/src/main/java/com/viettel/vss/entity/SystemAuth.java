package com.viettel.vss.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "system_auths")
public class SystemAuth extends BaseEntity {

	@Basic
	@Column(name = "auth_type")
	private String authType;

	@Basic
	@Column(name = "credential")
	private String credential;

    @Basic
    @Column(name = "last_fetch_at")
    private Date lastFetchAt;

    @Basic
    @Column(name = "next_fetch_at")
    private Date nextFetchAt;

    @Basic
    @Column(name = "fetch_duration")
    private Long fetchDuration;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_id", unique = true)
    @JsonIgnore
    private System system;

    @OneToMany(mappedBy = "systemAuth", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SystemToken> systemTokens;
}
