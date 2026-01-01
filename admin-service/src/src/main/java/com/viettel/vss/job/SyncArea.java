package com.viettel.vss.job;

import com.viettel.sharelock.config.ShareLock;
import com.viettel.sharelock.dto.JobConfig;
import com.viettel.sharelock.dto.JobParam;
import com.viettel.sharelock.service.ShareLockJob;
import com.viettel.vss.util.MessageCommon;

import com.viettel.sharelock.service.LogRunningJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@ShareLock(description = "Đồng bộ thông tin vùng" )
public class SyncArea extends ShareLockJob<JobParam, JobConfig> {
    @Autowired
    MessageCommon messageCommon;

    @Override
    public JobParam getDefaultParam() {
        JobParam jobParam = new JobParam();
        jobParam.setFromDate(new Date());
        jobParam.setToDate(new Date());
        return jobParam;
    }

    @Override
    public JobConfig getJobConfig() {
        return null;
    }


    @Override
    public void doRun(JobParam params, JobConfig config, LogRunningJob logJob) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logJob.setLog("dfsdf");
    }
}
