package com.viettel.vss.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.viettel.vss.util.DateUtil;
import lombok.Data;
import lombok.Getter;
import org.joda.time.DateTimeConstants;

import java.util.Date;

@Data
public class UserExportDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    private String createdAt;
    private String updatedAt;

    public UserExportDto(Long id, String username, String fullName, String email, Long statusId, String statusName, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.status = new Status(statusId, statusName);
        this.createdAt = DateUtil.convertDateToString(createdAt, "dd/MM/yyyy");
        this.updatedAt = DateUtil.convertDateToString(updatedAt, "dd/MM/yyyy");
    }

    public UserExportDto(Long id, String username, String fullName, String email, Long statusId, String statusName,
                         String description, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.status = new Status(statusId, statusName);
        this.description = description;
        this.createdAt = DateUtil.convertDateToString(createdAt, "dd/MM/yyyy");
        this.updatedAt = DateUtil.convertDateToString(updatedAt, "dd/MM/yyyy");
    }

    @Getter
    public static class Status {
        private final Long id;
        private final String name;

        public Status(Long id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}
