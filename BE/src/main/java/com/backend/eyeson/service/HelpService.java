package com.backend.eyeson.service;


import com.backend.eyeson.entity.AngelInfoEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.repository.AngelRepository;
import com.backend.eyeson.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final AngelRepository angelRepository;

    private final FirebaseService firebaseService;


    // 도움 요청
    public boolean requestHelp(long userSeq, char gender) throws IOException {
        // gender가 같은 엔젤들 가져오기
        Optional<List<AngelInfoEntity>> angelList = angelRepository.findAllByAngelGender(gender);

        // 현재 시간
        int hour = LocalDateTime.now().getHour();

        String day = String.valueOf(LocalDate.now().getDayOfWeek());
        int days = 0;
        switch (day){
            case "MONDAY":
                days = 1;
                break;
            case "TUESDAY":
                days = 2;
                break;
            case "WEDNESDAY":
                days = 3;
                break;
            case "THURSDAY":
                days = 4;
                break;
            case "FRIDAY":
                days = 5;
                break;
            case "SATURDAY":
                days = 6;
                break;
            case "SUNDAY":
                days = 0;
                break;
        }

        System.out.println("시간 " + hour);
        System.out.println("요일 " + days);

        List<AngelInfoEntity> canAngelList = new ArrayList<>();

        // 시간으로 엔젤 필터링
        for(int i=0; i<angelList.get().size();i++){
            AngelInfoEntity angelInfoEntity = angelList.get().get(i);

            // 2진수로 바꾸기
            int angelDay = angelInfoEntity.getAngelAlarmDay();
            String binaryDay = Integer.toBinaryString(angelDay);

            String res = "";
            if(binaryDay.length() !=7){
                for(int z=0; z<7-binaryDay.length(); z++){
                    res+="0";
                }
                for(int j=0;j<binaryDay.length();j++){
                    res += binaryDay.charAt(j);
                }
            }
            
            char[] chArray = res.toCharArray();
            
            // 요일에 있고 시간안에 있으면 배열에 추가
            if(chArray[days] == '1' && (angelInfoEntity.getAngelAlarmStart().getHour() <= hour && angelInfoEntity.getAngelAlarmEnd().getHour() >= hour)){
                System.out.println("시작 시간 : " + angelInfoEntity.getAngelAlarmStart().getHour());
                System.out.println("끝나는 시간 : " + angelInfoEntity.getAngelAlarmEnd().getHour());
                System.out.println("지금 시간 : " + hour);
                System.out.println("요일 : " + days);
                canAngelList.add(angelInfoEntity);
            }
        }

        // 알림 보내기
        for(int i=0; i<canAngelList.size(); i++){
            // fcm 토큰
            String fcmToken = canAngelList.get(i).getUserSeq().getUserFcm();
            // 알림 제목
            String title = "도움 요청이 도착했어요 !";
            // 알림 내용
            String body = "사용자를 따뜻한 마음으로 도와주세요 !";

            firebaseService.sendMessageTo(fcmToken, title, body);
        }

        if(canAngelList.size() == 0) return false;
        else return true;
    }

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
    public boolean finishHelp(long userSeq) {

        // 엔젤 찾기
        Optional<AngelInfoEntity> angelInfoEntity = angelRepository.findByUserSeq(userSeq);

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
