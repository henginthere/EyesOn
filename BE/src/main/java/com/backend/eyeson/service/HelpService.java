package com.backend.eyeson.service;


import com.backend.eyeson.entity.AngelInfoEntity;
import com.backend.eyeson.repository.AngelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HelpService {


    private final AngelRepository angelRepository;

    private final FirebaseService firebaseService;


    // 도움 요청
    public boolean requestHelp(char gender) throws IOException {
        // gender가 같은 엔젤들 가져오기
        Optional<List<AngelInfoEntity>> angelList;

        // 만약 모든 사용자를 원할 경우
        if(gender == 'd'){
            angelList = Optional.ofNullable(angelRepository.findAll());
        }else{
            angelList = angelRepository.findAllByAngelGender(gender);
        }

        // 현재 시간
        int hour = LocalDateTime.now().getHour();

        String day = String.valueOf(LocalDate.now().getDayOfWeek());
        int todayNum = 0;
        switch (day){
            case "MONDAY":
                todayNum = 32;
                break;
            case "TUESDAY":
                todayNum = 16;
                break;
            case "WEDNESDAY":
                todayNum = 8;
                break;
            case "THURSDAY":
                todayNum = 4;
                break;
            case "FRIDAY":
                todayNum = 2;
                break;
            case "SATURDAY":
                todayNum = 1;
                break;
            case "SUNDAY":
                todayNum = 64;
                break;
            default:
                todayNum = 0;
                break;
        }

        // 가능한 엔젤 리스트
        List<AngelInfoEntity> canAngelList = new ArrayList<>();

        // 시간으로 엔젤 필터링
        for(int i=0; i<angelList.get().size();i++){
            AngelInfoEntity angelInfoEntity = angelList.get().get(i);

            // 사용자 시간 받아오기
            int angelDay = angelInfoEntity.getAngelAlarmDay();

            // 요일에 있고 시간안에 있으면 배열에 추가
            if(angelInfoEntity.isAngelActive() && ((todayNum & angelDay) != 0) && (angelInfoEntity.getAngelAlarmStart()) <= hour && angelInfoEntity.getAngelAlarmEnd() >= hour){
                canAngelList.add(angelInfoEntity);
            }
        }

        // 알림 제목
        String title = "도움 요청이 도착했어요 !";

        // 알림 내용
        String body = "사용자를 따뜻한 마음으로 도와주세요 !";
        
        // click_action 처리
        String action = "AngelHelp";

        // 알림 보내기
        for(int i=0; i<canAngelList.size(); i++){
            // fcm 토큰
            String fcmToken = canAngelList.get(i).getUserEntity().getUserFcm();
            firebaseService.sendMessageTo(fcmToken, title, body, action);
        }

        if(canAngelList.size() == 0) return false;
        else return true;
    }


    // 도움 종료
    public boolean finishHelp(long userSeq) {

        // 엔젤 찾기
        Optional<AngelInfoEntity> angelInfoEntity = angelRepository.findByUserEntity_UserSeq(userSeq);

        if(!angelInfoEntity.isPresent()) return false;

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
