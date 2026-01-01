package com.viettel.vss.constant.common.customValidator.validator;
import com.viettel.vss.constant.common.customValidator.SizeMessageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class SizeMessageCommonValidator implements ConstraintValidator<SizeMessageCommon, String> {

    private int min;
    private int max;
    private String code;
    private String fieldName;

    @Autowired
    private MessageCommonValidator messageCommon;

    @Override
    public void initialize(SizeMessageCommon annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
        this.code = annotation.code();
        this.fieldName = annotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // NotBlank sẽ check null

        int length = value.length();
        if (length < min || length > max) {
            String message = messageCommon.getMessage(code, LocaleContextHolder.getLocale(), fieldName, min, max);

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                   .addConstraintViolation();
            return false;
        }
        return true;
    }
}
