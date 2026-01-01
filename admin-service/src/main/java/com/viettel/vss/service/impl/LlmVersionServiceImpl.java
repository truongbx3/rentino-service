package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.LlmVersionDto;
import com.viettel.vss.entity.LlmVersion;
import com.viettel.vss.repository.LlmVersionRepository;
import com.viettel.vss.service.LlmVersionService;
import org.springframework.stereotype.Service;

@Service
public class LlmVersionServiceImpl extends BaseServiceImpl<LlmVersion, LlmVersionDto> implements LlmVersionService {

    private final LlmVersionRepository llmVersionRepository;

    public LlmVersionServiceImpl(LlmVersionRepository llmVersionRepository) {
        super(llmVersionRepository, LlmVersion.class, LlmVersionDto.class);
        this.llmVersionRepository = llmVersionRepository;
    }
}