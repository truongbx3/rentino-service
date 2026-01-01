package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import javax.persistence.*;

import java.util.List;

@Entity
@Data
@Table(name = "menu")
public class Menu extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Basic
	@Column(name = "menu_name")
	private String menuName;

	@Basic
	@Column(name = "menu_code")
	private String menuCode;

	@Basic
	@Column(name = "url")
	private String url;

	@Basic
	@Column(name = "is_show")
	private Boolean isShow;

	@Basic
	@Column(name = "index_order")
	private Integer indexOrder;


	@Basic
	@Column(name = "description")
	private String description;

	@Basic
	@Column(name = "form_name")
	private String formName;

	@Basic
	@Column(name = "icon")
	private String icon;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @ManyToOne
    @JoinColumn(name = "module_id")
	private Module module;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "menu_function",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private List<Function> functions;
}
