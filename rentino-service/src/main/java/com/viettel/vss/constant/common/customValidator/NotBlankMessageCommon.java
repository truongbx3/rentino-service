package com.viettel.vss.constant.common.customValidator;

import com.viettel.vss.constant.common.customValidator.validator.NotBlankMessageCommonValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankMessageCommonValidator.class)
public @interface NotBlankMessageCommon {
    String code();
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName();
}