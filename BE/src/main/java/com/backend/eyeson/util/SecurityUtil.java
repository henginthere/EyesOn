package com.backend.eyeson.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static long getCurrentMemberSeq(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        return Long.parseLong(authentication.getName());
    }

    public static String getCurrentMemberRole(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        return authentication.getAuthorities().toString();
    }

    public static String getCurrentMemberEmail(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        return authentication.getDetails().toString();
    }
}