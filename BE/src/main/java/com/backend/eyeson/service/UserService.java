package com.backend.eyeson.service;

import com.backend.eyeson.controller.UserController;
import com.backend.eyeson.dto.RequestRegistDto;
import com.backend.eyeson.dto.ResponseLoginDto;
import com.backend.eyeson.entity.AuthorityEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.repository.AuthorityRepository;
import com.backend.eyeson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    // 회원가입
    public ResponseLoginDto signup(RequestRegistDto requestRegistDto) {
        // 만일 이미 있는 이메일인 경우 실패
        UserEntity checkUser = userRepository.getByUserEmail(requestRegistDto.getUserEmail());
        if(checkUser != null) return null;

        // gender enum으로 변경
        // m : men, w : women
        char gender = 0;
        if(requestRegistDto.getUserGender().equals("Women")) gender = 'w';
        else if(requestRegistDto.getUserGender().equals("Men")) gender = 'm';

        // 권한 설정
        AuthorityEntity authorityEntity;

        boolean role;

        // true : 시각장애인, false : 엔젤
        if(!requestRegistDto.isUserRole()){
            role = false;
            authorityEntity = AuthorityEntity.builder()
                    .authorityName("ROLE_ANGEL")
                    .build();

            // 로그로 바꾸기
            System.out.println(authorityEntity.getAuthorityName());
        }
        else{
            role = true;
            authorityEntity = AuthorityEntity.builder()
                    .authorityName("ROLE_BLIND")
                    .build();

            // 로그로 바꾸기
            System.out.println(authorityEntity.getAuthorityName());
        }

        // 권한 저장
        authorityRepository.save(authorityEntity);

        UserEntity userEntity = UserEntity.builder().
                userName(requestRegistDto.getUserName()).
                userFcm(requestRegistDto.getUserFcm()).
                userEmail(requestRegistDto.getUserEmail()).
                userGender(gender).
                userDate(LocalDateTime.now()).
                authorities(Collections.singleton(authorityEntity))
                .build();

        userRepository.save(userEntity);

        // 회원가입 후 로그인까지 처리
        ResponseLoginDto responseLoginDto = login(requestRegistDto.getUserEmail());
        return responseLoginDto;
    }

    // 로그인
    public ResponseLoginDto login(String userEmail){

        // userEmail로 계정 찾기
        UserEntity userEntity = userRepository.findByUserEmail(userEmail);

        // 계정이 없는 경우
        if(userEntity == null){
            return null;
        }

        ResponseLoginDto responseLoginDto = authService.authorize(userEmail);

        return responseLoginDto;
    }
}
