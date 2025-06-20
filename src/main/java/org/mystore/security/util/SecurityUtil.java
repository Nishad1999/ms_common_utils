package org.mystore.security.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {


    public static boolean hasAuthority(String requiredAuthority) {
        Authentication auth = getAuth();
        if (auth == null) {
            return false;
        }

        return auth.getAuthorities().stream().anyMatch(granted -> granted.getAuthority().equals(requiredAuthority));
    }

    public static boolean hasAnyAuthority(String... requiredAuthorities) {
        Authentication auth = getAuth();
        if (auth == null || requiredAuthorities == null || requiredAuthorities.length == 0) {
            return false;
        }

        Set<String> required = new HashSet<>(Arrays.asList(requiredAuthorities));

        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(required::contains);
    }

    private static Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() ? auth : null;
    }

}
