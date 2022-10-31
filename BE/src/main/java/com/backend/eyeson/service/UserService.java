package com.backend.eyeson.service;

import com.backend.eyeson.controller.UserController;
import com.backend.eyeson.dto.RequestRegistDto;
import com.backend.eyeson.dto.ResponseLoginDto;
import com.backend.eyeson.entity.AuthorityEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.repository.AuthorityRepository;
import com.backend.eyeson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseLoginDto signup(String email, String fcmToken) {
        // 만일 이미 있는 이메일인 경우 실패
        String g_id[] = email.split("@");
        String pass = g_id[0];

//        // gender enum으로 변경
//        // m : men, w : women
//        char gender = 0;
//        if(requestRegistDto.getUserGender().equals("Women")) gender = 'w';
//        else if(requestRegistDto.getUserGender().equals("Men")) gender = 'm';

        // 회원 가입 시 gender와 role은 default로 저장,


        // 권한 설정
        AuthorityEntity authorityEntity;

        boolean role;
        authorityEntity = AuthorityEntity.builder()
                .authorityName("ROLE_ADMIN")
                .build();
//        // true : 시각장애인, false : 엔젤
//        if(!requestRegistDto.isUserRole()){
//            role = false;
//            authorityEntity = AuthorityEntity.builder()
//                    .authorityName("ROLE_ANGEL")
//                    .build();
//
//            // 로그로 바꾸기
//            System.out.println(authorityEntity.getAuthorityName());
//        }
//        else{
//            role = true;
//            authorityEntity = AuthorityEntity.builder()
//                    .authorityName("ROLE_BLIND")
//                    .build();
//
//            // 로그로 바꾸기
//            System.out.println(authorityEntity.getAuthorityName());
//        }

        // 권한 저장
        authorityRepository.save(authorityEntity);

        UserEntity userEntity = UserEntity.builder().
                userFcm(fcmToken).
                userEmail(email).
                userPass(passwordEncoder.encode(pass)).
                userGender('d').
                userDate(LocalDateTime.now()).
                authorities(Collections.singleton(authorityEntity))
                .build();

        userRepository.save(userEntity);

        // 회원가입 후 로그인까지 처리
        ResponseLoginDto responseLoginDto = login(email);
        return responseLoginDto;
    }

    // 로그인
    public ResponseLoginDto login(String userEmail){

        // 계정이 없는 경우
        if(!userRepository.findByUserEmail(userEmail).isPresent()){
            return null;
        }

        ResponseLoginDto responseLoginDto = authService.authorize(userEmail);

        return responseLoginDto;
    }
}
