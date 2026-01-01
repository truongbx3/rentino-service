package com.viettel.vss.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class Localization {

    public Locale getCurrentLocale() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String headerLang = request.getHeader("Web-Language");

        if (headerLang == null || headerLang.isEmpty()) {
            return new Locale("vi");
        }

        Locale locale = Locale.forLanguageTag(headerLang);
        if (locale == null || locale.getLanguage().isEmpty()) {
            return new Locale("vi");
        }

        return locale;
    }
}
