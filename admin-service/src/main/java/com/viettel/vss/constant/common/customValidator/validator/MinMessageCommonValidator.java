package com.viettel.vss.constant.common.customValidator.validator;
import com.viettel.vss.constant.common.customValidator.MinMessageCommon;
import com.viettel.vss.constant.common.customValidator.NotBlankMessageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class MinMessageCommonValidator implements ConstraintValidator<MinMessageCommon, Number> {

    private long minValue;
    private String code;
    private String fieldName;

    @Autowired
    private MessageCommonValidator messageCommon;

    @Override
    public void initialize(MinMessageCommon annotation) {
        this.minValue = annotation.value();
        this.code = annotation.code();
        this.fieldName = annotation.fieldName();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (value.longValue() < minValue) {
            String message = messageCommon.getMessage(code, LocaleContextHolder.getLocale(), fieldName, minValue);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                   .addConstraintViolation();
            return false;
        }
        return true;
    }
}
