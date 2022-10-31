package com.backend.eyeson.service;

import com.backend.eyeson.dto.ResponseAngelInfoDto;
import com.backend.eyeson.dto.ResponseLoginDto;
import com.backend.eyeson.entity.AngelInfoEntity;
import com.backend.eyeson.entity.AuthorityEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.mapper.AngelMapper;
import com.backend.eyeson.repository.AngelRepository;
import com.backend.eyeson.repository.AuthorityRepository;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AngelRepository angelRepository;

    private final AuthorityRepository authorityRepository;

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseLoginDto signup(String email, String fcmToken) {
        // 만일 이미 있는 이메일인 경우 실패
        String g_id[] = email.split("@");
        String pass = g_id[0];

        // 회원 가입 시 gender와 role은 default로 저장,

        // 권한 설정
        AuthorityEntity authorityEntity;

        authorityEntity = AuthorityEntity.builder()
                .authorityName("ROLE_ADMIN")
                .build();

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

    public ResponseLoginDto register(String role, char gender){
        long userSeq = SecurityUtil.getCurrentMemberSeq();
        UserEntity userEntity = userRepository.findByUserSeq(userSeq).get();

        Set<AuthorityEntity> set = new HashSet<>();
        AuthorityEntity authorityEntity = AuthorityEntity.builder().
                authorityName(role).
                build();
        set.add(authorityEntity);
        userEntity.setUserGender(gender);
        userEntity.setAuthorities(set);

        userRepository.save(userEntity);
        if(role.equals("ROLE_ANGEL") && angelRepository.findByUserEntity_UserSeq(userSeq).isEmpty()){
            AngelInfoEntity angelInfoEntity = new AngelInfoEntity();
            angelInfoEntity.setUserEntity(userEntity);
            angelRepository.save(angelInfoEntity);
        }

        String email = userRepository.findByUserSeq(SecurityUtil.getCurrentMemberSeq()).get().getUserEmail();
        ResponseLoginDto responseLoginDto = login(email);
        return responseLoginDto;
    }

    public ResponseAngelInfoDto getInfo(){
        long userSeq = SecurityUtil.getCurrentMemberSeq();
        AngelInfoEntity angelInfoEntity = angelRepository.findByUserEntity_UserSeq(userSeq).get();
        return AngelMapper.INSTANCE.toDto(angelInfoEntity);
    }
}
