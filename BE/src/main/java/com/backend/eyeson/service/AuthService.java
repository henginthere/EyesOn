package com.backend.eyeson.service;


import com.backend.eyeson.dto.ResponseLoginDto;
import com.backend.eyeson.dto.TokenDto;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.jwt.TokenProvider;
import com.backend.eyeson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    UserRepository userRepository;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    // 로그인 관련 메서드
    public ResponseLoginDto authorize(String email) {


        String pass = email.split("@")[0];

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email,pass);
        //실제 검증이 일어나는 부분
        //authenticate 메서드가 실행될 때

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        System.out.println("권한 : "+authorities);
        TokenDto tokenDto;

        UserEntity user = userRepository.findByUserEmail(email).get();

        if(user==null){
            return null;
        }

        else{
            tokenDto = tokenProvider.createUserToken(authentication.getName(), authorities);
        }

        return new ResponseLoginDto(tokenDto);

    }

    // 토큰 재발급 관련 메서드
    public TokenDto reissue(String requestAccessToken, String requestRefreshToken) {
        if (!tokenProvider.validateToken(requestRefreshToken)) {
//            throw new UnauthorizedException("유효하지 않은 RefreshToken 입니다");
            //재로그인 요청반환

        }

        Authentication authentication = tokenProvider.getAuthentication(requestRefreshToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);
        String userEmail = userRepository.findByUserSeq(Long.parseLong(authentication.getName())).getUserEmail();
        return tokenProvider.createUserToken(userEmail,authorities);

    }

    // 권한 가져오기
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
