package com.viettel.vss.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.viettel.vss.base.SortField;
import com.viettel.vss.constant.CommonConstants;
import lombok.Data;

import javax.persistence.FetchType;
import java.util.Date;
import java.util.List;

@Data
public class SearchUserDto {
    private List<Long> positionId;
    private List<Long> organizationIds;
    private List<Long> roleIds;
    private List<String> usernameList;
    private String keyword;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String comment;
    private List<String> contract;
    private List<String> objectType;
    private List<String> status;
    private Boolean isActive;
    private FetchType fetchType = FetchType.EAGER;
    private List<String> updatedBy;
    private List<String> createdBy;
    private List<String> dnNames;
    private Boolean isRole;
    private Integer gender;
    private Boolean isManager;
    private Boolean isKeyMember;

    private List<String> staffRole;
    private List<String> staffLevel;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date fromCreatedDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date toCreatedDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date fromUpdatedDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date toUpdatedDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date fromStartDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date toStartDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date fromEndDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date toEndDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date fromEntryDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date toEntryDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date fromBirthDate;

    @JsonFormat(pattern = CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY, timezone = CommonConstants.DATE_TIME_FORMAT.TIME_ZONE)
    private Date toBirthDate;

    private List<SortField> sortField;
}
