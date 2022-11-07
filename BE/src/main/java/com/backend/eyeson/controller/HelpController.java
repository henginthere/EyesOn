package com.backend.eyeson.controller;

import com.backend.eyeson.dto.Token;
import com.backend.eyeson.service.FirebaseService;
import com.backend.eyeson.service.HelpService;
import com.backend.eyeson.util.ResponseFrame;
import com.backend.eyeson.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

@Api("HelpController")
@RestController
@RequestMapping("/help")
@RequiredArgsConstructor
public class HelpController {

    // 시각장애인 userSeq 저장 Queue
    Queue<Long> blindQueue = new LinkedList<>();

    private final FirebaseService firebaseService;


    private final HelpService helpService;

    /**
     * 도움 요청
     *
     * @param gender
     * @return Object
     */

    @ApiOperation(value = "도움 요청", response = Object.class)
    @PostMapping("/{gender}")
    public ResponseFrame<?> requestHelp(@PathVariable("gender") char gender) throws IOException {

        // userSeq 가져오기
        long userSeq = SecurityUtil.getCurrentMemberSeq();

        // 알림보내기
        boolean check = helpService.requestHelp(gender);

        if(check) {
            // queue에 추가
            blindQueue.add(userSeq);
            return ResponseFrame.of(userSeq, "도움 요청 성공");
        }else{
            return ResponseFrame.of(HttpStatus.BAD_REQUEST, "엔젤이 없어서 도움 요청 실패");
        }
    }

    /**
     * 도움 응답
     *
     * @param
     * @return Object
     */

    @ApiOperation(value = "도움 응답", response = Object.class)
    @GetMapping("")
    public ResponseFrame<?> responseHelp(){

        // 만약 queue가 비어있으면 false
        boolean check = false;

        // 시각장애인 userSeq
        long userSeq = 0;

        // queue가 비어있지 않으면 poll하고 userSeq ( 시각장애인 ) 응답해주기
        if(!blindQueue.isEmpty()){
            userSeq = blindQueue.poll();
            check = true;
        }

        if(check){
            return ResponseFrame.of(userSeq, "도움 응답 성공");
        }else {
            return ResponseFrame.of(HttpStatus.BAD_REQUEST, "도움 요청한 기록이 없습니다. 도움 응답 실패");
        }
    }

    /**
     * 도움 종료
     *
     * @param
     * @return Object
     */

    @ApiOperation(value = "도움 종료", response = Object.class)
    @PutMapping("/finish")
    public ResponseFrame<?> finishHelp(){

        // userSeq 가져오기
        long userSeq = SecurityUtil.getCurrentMemberSeq();
        boolean check = helpService.finishHelp(userSeq);

        if(!check){
            return ResponseFrame.of(HttpStatus.BAD_REQUEST, "도움 종료 성공");
        }else {
            return ResponseFrame.of(HttpStatus.OK, "도움 종료 성공");
        }
    }


    @ApiOperation(value = "알림 테스트")
    @PostMapping("/alarm")
    public void checkAlarm(@RequestBody Token token) throws IOException {
        firebaseService.sendMessageTo(token.getFcmToken(), token.getTitle(), token.getBody(), token.getClickAction());
    }
}

