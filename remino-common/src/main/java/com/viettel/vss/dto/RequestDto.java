package com.viettel.vss.dto;

import com.viettel.vss.base.Condition;
import com.viettel.vss.base.SortField;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
    int page;
    int size;
    private List<Condition> lsCondition;
    private List<SortField> sortField;
}
