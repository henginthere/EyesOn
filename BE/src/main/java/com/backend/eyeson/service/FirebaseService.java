package com.backend.eyeson.service;

import com.backend.eyeson.dto.FcmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FirebaseService {

    private final ObjectMapper objectMapper;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/eyeson-8f746/messages:send";


    // FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        // GoogleApi를 사용하기 위해 oAuth2를 이용해 인증한 대상을 나타내는객체
        GoogleCredentials googleCredentials = GoogleCredentials
                // 서버로부터 받은 service key 파일 활용
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                // 인증하는 서버에서 필요로 하는 권한 지정
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        String token = googleCredentials.getAccessToken().getTokenValue();

        System.out.println(token);
        return token;
    }

    // 메세지 생성
    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage.Notification noti = new FcmMessage.Notification(title, body, null);
        FcmMessage.Data data = new FcmMessage.Data(".view.angel.help.AngelHelpActivity");
        FcmMessage.Message message = new FcmMessage.Message(noti, data, targetToken);
        FcmMessage fcmMessage = new FcmMessage(false, message);

        // 직렬화
        return objectMapper.writeValueAsString(fcmMessage);
    }


    // targetToken에 해당하는 device로 FCM 푸시 알림 전송
    public void
    sendMessageTo(String targetToken, String title, String body) throws IOException {
        // 엔젤 정보 받아서 시간 확인
        LocalDateTime str = LocalDateTime.now();
        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                // 전송 토큰 추가
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
    }
}

