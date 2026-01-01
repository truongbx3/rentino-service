package com.viettel.vss.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserCrudRequest {
    private Long id;

    @NotNull
    @Length(max = 50)
    private String userName;

    private String empCode;

    private String fullName;

    @Length(max = 500)
    private String email;

    private String status;

    private Date startDate;

    private Date endDate;

    private Date birthDay;

    private String staffRole;

    private String staffLevel;

    private Boolean isActive;

    private String comment;

    private Date contractDate;
}
