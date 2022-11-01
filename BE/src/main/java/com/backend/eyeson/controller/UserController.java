package com.backend.eyeson.controller;


import com.backend.eyeson.dto.GoogleLoginDto;
import com.backend.eyeson.dto.RequestRGDto;
import com.backend.eyeson.dto.RequestRegistDto;
import com.backend.eyeson.dto.ResponseLoginDto;
import com.backend.eyeson.jwt.TokenProvider;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.service.AuthService;
import com.backend.eyeson.service.UserService;
import com.backend.eyeson.util.ResponseFrame;
import com.backend.eyeson.util.SecurityUtil;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sun.istack.ByteArrayDataSource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

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
            ResponseLoginDto responseLoginDto = userService.login(userEmail);
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
        ResponseLoginDto responseLoginDto = userService.register(role, gender);
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
}
