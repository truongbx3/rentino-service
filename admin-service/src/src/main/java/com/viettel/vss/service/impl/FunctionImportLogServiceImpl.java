package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.function.FunctionImportLogDto;
import com.viettel.vss.entity.FunctionImportLog;
import com.viettel.vss.repository.FunctionImportLogRepository;
import com.viettel.vss.service.FunctionImportLogService;
import org.springframework.stereotype.Service;

@Service
public class FunctionImportLogServiceImpl extends BaseServiceImpl<FunctionImportLog, FunctionImportLogDto> implements FunctionImportLogService {

    private final FunctionImportLogRepository functionImportLogRepository;

    public FunctionImportLogServiceImpl(FunctionImportLogRepository functionImportLogRepository) {
        super(functionImportLogRepository, FunctionImportLog.class, FunctionImportLogDto.class);
        this.functionImportLogRepository = functionImportLogRepository;
    }
}