package com.viettel.vss.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public interface ReportService {
    ByteArrayOutputStream genXlsxLocal(Map<String, Object> data, String templateRelativePathAndName) throws IOException;
}