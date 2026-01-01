package com.viettel.vss.constant.common.customValidator;

import com.viettel.vss.constant.common.customValidator.validator.MinMessageCommonValidator;
import com.viettel.vss.constant.common.customValidator.validator.SizeMessageCommonValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinMessageCommonValidator.class)
public @interface MinMessageCommon {
    String code();
    long value();
    String fieldName() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

