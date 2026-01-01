package com.viettel.vss.service;

import com.viettel.vss.dto.attach_file.AttachFileDTO;

public interface ContractService {
    AttachFileDTO createContract (String phone,String transactionId) throws Exception;
}
