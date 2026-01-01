package com.viettel.vss.service.impl;

import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.service.ReportService;
import lombok.SneakyThrows;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @SneakyThrows
    @Override
    public ByteArrayOutputStream genXlsxLocal(Map<String, Object> data, String templateRelativePathAndName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateRelativePathAndName);
        if (in == null) {
            throw new BusinessException("Template file not found:" + templateRelativePathAndName);
        }
        Context context = PoiTransformer.createInitialContext();
        for (Map.Entry<String, Object> d : data.entrySet()) {
            if (d.getKey() != null && d.getValue() != null) {
                context.putVar(d.getKey(), d.getValue());
            }
        }

        JxlsHelper.getInstance().processTemplate(in, out, context);
        return out;
    }

}