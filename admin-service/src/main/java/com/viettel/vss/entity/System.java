package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "systems")
public class System extends BaseEntity {

	@Basic
	@Column(name = "name")
	private String name;

	@Basic
	@Column(name = "base_url")
	private String baseUrl;

	@Basic
	@Column(name = "description")
	private String description;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @OneToOne(mappedBy = "system", cascade = CascadeType.ALL)
    private SystemAuth systemAuth;

//    public void setAuth(SystemAuthEntity systemAuth) {
//        this.systemAuth = systemAuth;
//        if (systemAuth != null) {
//            systemAuth.setSystem(this);
//        }
//    }
}
