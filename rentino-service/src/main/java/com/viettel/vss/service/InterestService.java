package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.BorrowDto;
import com.viettel.vss.dto.InterestDto;
import com.viettel.vss.dto.UserBorrowDto;
import com.viettel.vss.entity.Interest;

import java.math.BigDecimal;
import java.util.List;

public interface InterestService extends BaseService<Interest, InterestDto> {

    public List<InterestDto> getInterest(String type, Integer isFixed);

    public List<InterestDto> getEstimateCode( BorrowDto borrowDto);
    public UserBorrowDto saveBorrowInfo(String name, BorrowDto borrowDto);
}
