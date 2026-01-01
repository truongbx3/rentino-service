package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.BorrowDto;
import com.viettel.vss.dto.InterestDto;
import com.viettel.vss.dto.UserBorrowDto;
import com.viettel.vss.entity.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.InterestService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.MessageCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static java.util.stream.Collectors.*;


@Service
@Slf4j
public class InterestServiceImpl extends BaseServiceImpl<Interest, InterestDto> implements InterestService {


    @Autowired
    private MessageCommon messageCommon;
    private final InterestRepository interestRepository;
    private final SystemConfigRepository systemConfigRepository;
    private final UsersRepository usersRepository;
    private final DeviceInfoRepository deviceInfoRepository;
    private final UserBorrowRepository userBorrowRepository;

    public InterestServiceImpl(InterestRepository interestRepository, SystemConfigRepository systemConfigRepository, UsersRepository usersRepository, DeviceInfoRepository deviceInfoRepository,  UserBorrowRepository userBorrowRepository) {
        super(interestRepository, Interest.class, InterestDto.class);
        this.interestRepository = interestRepository;
        this.systemConfigRepository = systemConfigRepository;
        this.usersRepository = usersRepository;
        this.deviceInfoRepository = deviceInfoRepository;
        this.userBorrowRepository = userBorrowRepository;
    }

    @Override
    public List<InterestDto> getInterest(String type, Integer isFix) {
        List<Interest> lst = interestRepository.findInterest(type, new Date(), isFix);
        if (lst == null || lst.size() == 0) {
            return null;
        }

        return DataUtil.convertList(lst, x -> modelMapper.map(x, InterestDto.class));
    }

    @Override
    public List<InterestDto> getEstimateCode(BorrowDto borrowDto) {

        Long numberDate = getDateBetween(borrowDto.getFromDate(), borrowDto.getToDate());
        SystemConfig systemConfig = systemConfigRepository.findFirstByCodeAndIsDeleted("MOBILE_MAX_BORROW", 0);
        Integer value = getValue(systemConfig.getValue());
        if (borrowDto.getRate() == null || borrowDto.getRate().compareTo(new BigDecimal(value)) > 0 || borrowDto.getRate().compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException(BusinessExceptionCode.RATE_LIMIT_NOTVALID, messageCommon.getMessage(BusinessExceptionCode.RATE_LIMIT_NOTVALID));
        }
        List<InterestDto> lst = filterByCodeAndMaxBorrow(getInterest("MOBILE", null), borrowDto.getRate());
        lst.stream().map(interestDto -> {
            if (interestDto.getIsFixed() == 1) {
                interestDto.setEstimateValue(interestDto.getPrice().multiply(borrowDto.getEstimateValue()).divide(new BigDecimal(100)));
            } else {
                interestDto.setDailyValue(interestDto.getPrice().multiply(borrowDto.getEstimateValue()).divide(new BigDecimal(100)));
                interestDto.setEstimateValue(interestDto.getPrice().multiply(borrowDto.getEstimateValue()).multiply(new BigDecimal(numberDate)).divide(new BigDecimal(100)));
            }
            return interestDto;
        }).collect(toList());
        return lst;
    }

    @Override
    public UserBorrowDto saveBorrowInfo(String userName, BorrowDto borrowDto) {
        Optional<UserEntity> userEntity = usersRepository.findByPhone(userName);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        DeviceInfo deviceInfo = deviceInfoRepository.findFirstByUserIdAndTransactionIdOrderByCreatedDate(userEntity.get().getId(), borrowDto.getTransactionid());
        if (deviceInfo == null) {
            throw new BusinessException(BusinessExceptionCode.DEVICE_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.DEVICE_NOT_FOUND));
        }
        if (deviceInfo.getPrice().multiply(borrowDto.getRate()).divide(new BigDecimal(100)).compareTo(borrowDto.getEstimateValue()) != 0) {
            throw new BusinessException(BusinessExceptionCode.RATE_LIMIT_NOTVALID,
                    messageCommon.getMessage(BusinessExceptionCode.RATE_LIMIT_NOTVALID));
        }
        List<InterestDto> interestDtos = getEstimateCode(borrowDto);
        UserBorrow userBorrow = new UserBorrow();
        userBorrow.setUserId(userEntity.get().getId());
        userBorrow.setValueBorrow(borrowDto.getEstimateValue());
        userBorrow.setRate(borrowDto.getRate());
        userBorrow.setTransaction(deviceInfo.getTransactionId());
        BigDecimal totalFixedValue = interestDtos.stream().filter(interestDto -> interestDto.getIsFixed() == 1).map(InterestDto::getEstimateValue).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        userBorrow.setFixedFee(totalFixedValue);
        userBorrow.setBorrowFromDate(borrowDto.getFromDate());
        userBorrow.setBorrowToDate(borrowDto.getToDate());
        userBorrow.setTotalDate(getDateBetween(borrowDto.getFromDate(), borrowDto.getToDate()));
        userBorrow.setDailyFee(interestDtos.stream().filter(interestDto -> interestDto.getIsFixed() == 0).map(InterestDto::getDailyValue).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        userBorrow.setInterestFee(interestDtos.stream().filter(interestDto -> interestDto.getIsFixed() == 0).map(InterestDto::getEstimateValue).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        userBorrow.setTotalFee(userBorrow.getFixedFee().add(userBorrow.getInterestFee()));
        userBorrowRepository.save(userBorrow);
        return DataUtil.convertObject( userBorrowRepository.save(userBorrow), x -> modelMapper.map(x, UserBorrowDto.class));
    }

    public Integer getValue(String value) {
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public List<InterestDto> filterByCodeAndMaxBorrow(List<InterestDto> list, BigDecimal threshold) {
        return list.stream()
                // 1) chỉ lấy những item maxBorrow < threshold
                .filter(dto -> dto.getMaxBorrow().compareTo(threshold) <= 0)
                // 2) nhóm theo code, mỗi nhóm giữ phần tử maxBy maxBorrow
                .collect(groupingBy(InterestDto::getCode,
                        // tìm max theo getMaxBorrow()
                        collectingAndThen(maxBy(Comparator.comparing(InterestDto::getMaxBorrow)), Optional::get    // đảm bảo có giá trị vì đã filter
                        )))
                // 3) lấy kết quả map.values() thành List
                .values().stream().collect(toList());
    }

    public long getDateBetween(Date fromDate, Date toDate) {
        Duration duration = Duration.between(fromDate.toInstant(), toDate.toInstant()).abs();
       return duration.toDays() + 1;
    }
}
