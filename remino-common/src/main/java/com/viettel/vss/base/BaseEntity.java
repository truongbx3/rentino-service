package com.viettel.vss.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Truongbx7
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public  class BaseEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

	@Column(name = "is_deleted")
	private int isDeleted;


//    @Column(name = "created_user")
//    @CreatedBy
//    private String createdUser;
//
//    @CreatedDate
//    @Column(updatable = false, name = "created_date")
//    private Date createdDate;
//
//    @Column(name = "updated_user")
//    @LastModifiedBy
//    private String updatedUser;
//
//    @Column(name = "updated_date")
//    @LastModifiedDate
//    private Date updatedDate;
//
//    @PrePersist
//    public void prePersist() {
//        this.updatedDate = null;
//        this.updatedUser = null;
//    }
}
