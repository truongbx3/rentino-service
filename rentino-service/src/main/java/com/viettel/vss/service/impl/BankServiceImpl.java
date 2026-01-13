package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.BanksDto;
import com.viettel.vss.dto.DevicePriceDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.entity.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.BanksService;
import com.viettel.vss.service.ContractService;
import com.viettel.vss.service.MinioService;
import com.viettel.vss.service.WordToPdfService;
import com.viettel.vss.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class BankServiceImpl extends BaseServiceImpl<Banks, BanksDto> implements BanksService {


    private BanksRepository banksRepository;

    @Autowired
    MessageCommon messageCommon;

    public BankServiceImpl(BanksRepository banksRepository) {
        super(banksRepository, Banks.class, BanksDto.class);
        this.banksRepository = banksRepository;
    }


    @Override
    public List<BanksDto> getBanksList() {
        return DataUtil.convertList(banksRepository.getBanksInfo(), x -> modelMapper.map(x, BanksDto.class));
    }


}
