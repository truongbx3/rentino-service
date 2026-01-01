package com.viettel.vss.constant.common.customValidator.validator;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Locale;

@Service
@Scope(
        scopeName = "singleton"
)
public class MessageCommonValidator {
    private MessageSource messageSource;

    public MessageCommonValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getValueByMessageCode(String messageCode) {
        return this.messageSource.getMessage(messageCode, (Object[])null, new Locale("vi"));
    }

    public String getValueByMessageCode(String messageCode, Locale locale) {
        return this.messageSource.getMessage(messageCode, (Object[])null, locale);
    }

    public String getMessage(String messageCode, Object... params) {
        try {
            return String.format(this.getValueByMessageCode(messageCode), params);
        } catch (Exception var4) {
            return messageCode;
        }
    }

    public String getMessage(String messageCode, Locale locale, Object... params) {
        try {
            String template = this.getValueByMessageCode(messageCode, locale); // "Field {0} is required"
            return MessageFormat.format(template, params); // format với {0}, {1}...
        } catch (Exception e) {
            return messageCode;
        }
    }

}
