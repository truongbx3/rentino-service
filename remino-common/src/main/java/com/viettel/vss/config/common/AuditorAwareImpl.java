package com.viettel.vss.config.common;

import com.viettel.vss.config.bean.UserCustom;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserCustom principal = (UserCustom) authentication.getPrincipal();
            return Optional.of(principal.getUsername());
        }

        return Optional.of("");
    }

}
