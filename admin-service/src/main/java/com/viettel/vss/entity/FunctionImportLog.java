package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "function_import_log")
public class FunctionImportLog extends BaseEntity {

    @Basic
    @Column(name = "file_name")
    private String fileName;
}
