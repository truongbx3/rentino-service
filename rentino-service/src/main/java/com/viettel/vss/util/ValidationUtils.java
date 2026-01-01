package com.viettel.vss.util;

import com.viettel.vss.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.viettel.vss.constant.BusinessExceptionCode.INVALID_PHONE_NUMBER;

@Component
@RequiredArgsConstructor
public class ValidationUtils {
    private final MessageCommon messageCommon;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public void validatePhoneNumber(String phone) {
        if (phone.matches("^\\d{10}$")) {
            if (!phone.startsWith("09") && !phone.startsWith("03") && !phone.startsWith("07") &&
                    !phone.startsWith("08") && !phone.startsWith("05")) {
                throw new BusinessException(INVALID_PHONE_NUMBER, messageCommon.getMessage(INVALID_PHONE_NUMBER));
            }
        } else if (phone.matches("^\\d{11}$")) {
            if (!phone.startsWith("02")) {
                throw new BusinessException(INVALID_PHONE_NUMBER, messageCommon.getMessage(INVALID_PHONE_NUMBER));
            }
        } else {
            throw new BusinessException(INVALID_PHONE_NUMBER, messageCommon.getMessage(INVALID_PHONE_NUMBER));
        }
    }


    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }


    public static boolean validateImportUserFormatFile(Row row){
        return "STT".equals(row.getCell(0).getStringCellValue()) &&
                "Tài khoản".equals(row.getCell(1).getStringCellValue()) &&
                "Email(*)".equals(row.getCell(2).getStringCellValue()) &&
                "Mật khẩu".equals(row.getCell(3).getStringCellValue());
    }

    public static boolean isValidSvgUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            URL parsed = new URL(url.trim());

            String protocol = parsed.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                return false;
            }

            String path = parsed.getPath();
            if (path == null) {
                return false;
            }

            return path.toLowerCase().endsWith(".svg");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            URL parsed = new URL(url.trim());
            String protocol = parsed.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
