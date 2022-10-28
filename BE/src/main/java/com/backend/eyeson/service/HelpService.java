package com.backend.eyeson.service;


import com.backend.eyeson.entity.AngelInfoEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.repository.AngelRepository;
import com.backend.eyeson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HelpService {

    private final UserRepository userRepository;

    private final AngelRepository angelRepository;


    // 도움 요청
//    public boolean requestHelp(char gender) {
//        // gender가 같은 엔젤들 가져오기
//
//
//
//    }

    // 도움 응답
    public long responseHelp(String email) {
        // 이메일로 사용자 seq 찾기
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(email);
        if (!userEntity.isPresent()) {
            return -1;
        } else {
            return userEntity.get().getUserSeq();
        }
    }

    // 도움 종료
    public boolean finishHelp(String email) {
        // 유저 정보 가져오기
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(email);

        // 엔젤 찾기
        Optional<AngelInfoEntity> angelInfoEntity = angelRepository.findByUserSeq(userEntity.get().getUserSeq());

        if(!userEntity.isPresent() || !angelInfoEntity.isPresent()) return false;

        // 바꿔주기
        // 카운팅 하기
        int cnt = angelInfoEntity.get().getAngelHelpCnt();
        cnt++;
        angelInfoEntity.get().setAngelHelpCnt(cnt);

        // 저장
        angelRepository.save(angelInfoEntity.get());

        return true;
    }
}
