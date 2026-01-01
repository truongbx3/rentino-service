package com.viettel.vss.dto.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class UserListDto {
    private Long id;
    private String username;
    private String empCode;
    private String fullName;
    private String email;
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public UserListDto(Long id, String username, String empCode, String fullName, String email, Long statusId, String statusName,
                       String description, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.empCode = empCode;
        this.fullName = fullName;
        this.email = email;
        this.status = new Status(statusId, statusName);
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserListDto(Long id, String username, String empCode, String fullName, String email, Long statusId, String statusName, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.empCode = empCode;
        this.fullName = fullName;
        this.email = email;
        this.status = new Status(statusId, statusName);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
