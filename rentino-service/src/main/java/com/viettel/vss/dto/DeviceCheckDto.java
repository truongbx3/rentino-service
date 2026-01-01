package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import com.viettel.vss.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCheckDto extends BaseDto {


    private Long id;

    private Long userId;

    private String transactionId;

    private String item;
    private String value;
    private Date createdDate;
	private int isDeleted;
}
