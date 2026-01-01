package com.viettel.vss.base;

import lombok.*;

/**
 * @author truongbx
 * @since 6/4/2021
 */
@Getter
@Setter
@NoArgsConstructor
public class SortField {
    String fieldName;
    String sort;

    public SortField(String fieldName, String sort) {
        this.fieldName = fieldName;
        this.sort = sort;
    }
}
