package com.springSecond.space.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    // Utility method to get the userId from SecurityContext
    public static String getUserIdFromSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Or any other field that holds the userId
        }
        return null; // Return null if the user is not authenticated or principal is not available
    }
}
