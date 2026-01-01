package com.viettel.vss.constant.common.customValidator.validator;

import com.viettel.vss.constant.common.customValidator.NotBlankMessageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankMessageCommonValidator implements ConstraintValidator<NotBlankMessageCommon, Object> {
    @Autowired
    private MessageCommonValidator messageCommon;

    private String code;
    private String fieldName;

    @Override
    public void initialize(NotBlankMessageCommon annotation) {
        this.code = annotation.code();
        this.fieldName = annotation.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value == null) {
            valid = false; // Object null → invalid
        } else if (value instanceof String) {
            valid = !((String) value).trim().isEmpty(); // String rỗng → invalid
        }else if (value instanceof java.util.Collection) {
            valid = !((java.util.Collection<?>) value).isEmpty();
        }

        if (!valid) {
            String message = messageCommon.getMessage(code, LocaleContextHolder.getLocale(), fieldName);

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
