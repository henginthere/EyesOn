package com.backend.eyeson.controller;

import com.backend.eyeson.service.HelpService;
import com.backend.eyeson.util.ResponseFrame;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Api("HelpController")
@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
public class HelpController {


    private final HelpService helpService;

    /**
     * 도움 요청
     *
     * @param gender
     * @return Object
     */

    @ApiOperation(value = "도움 요청", response = Object.class)
    @PostMapping("/{gender}")
    public ResponseFrame<?> requestHelp(HttpServletRequest request, @PathVariable("gender") char gender) throws IOException {
        ResponseFrame res;
        long userSeq = 0;
        boolean check = helpService.requestHelp(userSeq, gender);
        if(check) {
            return ResponseFrame.of(HttpStatus.OK, "도움 요청 성공");
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

    @ApiOperation(value = "도와 주기", response = Object.class)
    @GetMapping("")
    public ResponseFrame<?> responseHelp(HttpServletRequest request){

        // email 가져오기
        String email = "aa@gmail.com";
        long userSeq = helpService.responseHelp(email);
        if(userSeq == -1){
            return ResponseFrame.of(HttpStatus.BAD_REQUEST, "없는 엔젤입니다. 도움 응답 실패");
        }else {
            return ResponseFrame.of(userSeq, "도움 응답 성공");
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
    public ResponseFrame<?> finishHelp(HttpServletRequest request){

        // email 가져오기
        String email = "aa@gmail.com";
        boolean check = helpService.finishHelp(email);

        if(!check){
            return ResponseFrame.of(HttpStatus.BAD_REQUEST, "도움 종료 성공");
        }else {
            return ResponseFrame.of(HttpStatus.OK, "도움 종료 성공");
        }
    }
}

