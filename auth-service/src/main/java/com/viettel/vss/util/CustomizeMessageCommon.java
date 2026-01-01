package com.viettel.vss.util;

import com.viettel.vss.component.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Scope(
        scopeName = "singleton"
)
public class CustomizeMessageCommon extends MessageCommon {

    @Autowired
    private Localization localization;

    public CustomizeMessageCommon(MessageSource messageSource) {
        super(messageSource);
    }

    public String getMessage(String messageCode, Object... params) {
        try {
            return String.format(this.getValueByMessageCode(messageCode, localization.getCurrentLocale()), params);
        } catch (Exception var4) {
            return messageCode;
        }
    }
}
