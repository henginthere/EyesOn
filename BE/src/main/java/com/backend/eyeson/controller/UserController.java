package com.backend.eyeson.controller;


import com.backend.eyeson.dto.*;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.service.UserService;
import com.backend.eyeson.util.ResponseFrame;
import com.backend.eyeson.util.SecurityUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Api("UserController")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 로그인
     *
     * @param googleLoginDto
     * @return Object
     */

    @ApiOperation(value = "로그인", response = Object.class)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody GoogleLoginDto googleLoginDto) throws IOException, GeneralSecurityException {
        ResponseFrame<?> res;
        String userEmail;
        String fcmToken;
        String googleAccessToken = googleLoginDto.getGoogleAccessToken();
        System.out.println(googleAccessToken);
        fcmToken = googleLoginDto.getFcmToken();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleIdToken idToken = GoogleIdToken.parse(jsonFactory, googleAccessToken);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(new NetHttpTransport(),
                new JacksonFactory());
        if (verifier.verify(idToken)) {
            Payload payload = idToken.getPayload();
            userEmail = payload.getEmail();
        } else {
            //Invalid ID token
            System.out.println("만료된 토큰");
            res = ResponseFrame.of(HttpStatus.UNAUTHORIZED, "만료된 토큰으로 로그인에 실패하였습니다.");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        //t_user에 email 존재 여부 확인
        if (userRepository.findByUserEmail(userEmail) != null) {
            ResponseLoginDto responseLoginDto = userService.login(userEmail, fcmToken);
            if (responseLoginDto != null) {
                res = ResponseFrame.of(responseLoginDto, "로그인에 성공하였습니다.");
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else { //db에 이메일이 없으면 회원가입을 한다.
                responseLoginDto = userService.signup(userEmail, fcmToken);
                res = ResponseFrame.of(responseLoginDto, "회원가입을 성공했습니다.");
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);


    }

    /**
     * 성별, 역할 등록
     *
     * @param requestRGDto
     * @return Object
     */

    @ApiOperation(value = "성별, 역할 등록", response = Object.class)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RequestRGDto requestRGDto) throws IOException, GeneralSecurityException {
        ResponseFrame<?> res;
        String role = requestRGDto.getUserRole();
        char gender = requestRGDto.getUserGender();
        ResponseLoginDto responseLoginDto = userService.register(role, gender,"");
        res = ResponseFrame.of(responseLoginDto, "정보 등록에 성공하였습니다.");
        return new ResponseEntity<>(res, HttpStatus.OK);

    }
    

    /**
     * 회원탈퇴
     *
     * @param
     * @return Object
     */

    @ApiOperation(value = "회원탈퇴", response = Object.class)
    @DeleteMapping("/info")
    public ResponseFrame<?> dropUser(){

        // userSeq 가져오기
        long userSeq = SecurityUtil.getCurrentMemberSeq();
        userService.dropUser(userSeq);
        return ResponseFrame.of(HttpStatus.OK, "회원 탈퇴 성공");

    }

    /**
     * 엔젤 정보 조회
     *
     * @param
     * @return Object
     */
    @ApiOperation(value = "엔젤 정보 조회", response = ResponseAngelInfoDto.class)
    @GetMapping("/info")
    public ResponseEntity<?> getInfo(){
        ResponseFrame<?> res;
        ResponseAngelInfoDto responseAngelInfoDto = userService.getInfo();
        res = ResponseFrame.of(responseAngelInfoDto, "정보 조회에 성공하였습니다.");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    /**
     * 엔젤 정보 입력
     *
     * @param
     * @return Object
     */
    @ApiOperation(value = "엔젤 정보 입력", response = ResponseAngelInfoDto.class)
    @PutMapping("/angel")
    public ResponseEntity<?> setAngelInfo(@RequestBody RequestAngelInfoDto requestAngelInfoDto){
        ResponseFrame<?> res;
        int angelAlarmStart = requestAngelInfoDto.getAngelAlarmStart();
        int angelAlarmEnd = requestAngelInfoDto.getAngelAlarmEnd();
        int angelAlarmDay = requestAngelInfoDto.getAngelAlarmDay();
        boolean angelActive = requestAngelInfoDto.isAngelActive();
        ResponseAngelInfoDto responseAngelInfoDto = userService.setAngelInfo(angelAlarmStart, angelAlarmEnd, angelAlarmDay, angelActive);
        res = ResponseFrame.of(responseAngelInfoDto, "엔젤 정보 입력에 성공하였습니다.");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
