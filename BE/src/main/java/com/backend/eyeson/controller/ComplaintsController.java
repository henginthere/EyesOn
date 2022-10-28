package com.backend.eyeson.controller;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.util.ResponseFrame;
import com.backend.eyeson.util.ReverseGeocoding;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@Api(tags   = "민원 컨트롤러")
public class ComplaintsController {
    private final UserRepository userRepository;

//    public UserDto getLoginUser() {
//        UserEntity user = null;
//        try {
//            user = userRepo.findById(SecurityUtil.getCurrentMemberId()).get();
//        } catch (Exception e) {
//            user = new User();
//            user.setBirthday(1996);
//            user.setGender("male");
//            user.setId("anfidthtn");
//            user.setNickName("무량소수");
//            user.setPoint(1000);
//            user.setPass("1234");
//            user.setRegTime(LocalDateTime.now());
//        }
//
//        return UserDto.of(user);
//    }

    @ApiParam(value = "민원 등록")
    @PostMapping(value = "/register")
    public ResponseEntity<?> registerCom(@RequestBody ComplaintsDto params) throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "민원 등록 성공"), HttpStatus.OK);
    }

    @ApiParam(value = "신청 민원 전체 조회")
    //paging 처리
    @GetMapping(value = "/list")
    public ResponseEntity<?> listAll(String address) throws Exception{

        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "신청 민원 전체 조회"), HttpStatus.OK);
    }

    @ApiParam(value = "엔젤: 내가 처리한 민원 조회")
    //paging 처리
    @GetMapping(value = "/list/angel")
    public ResponseEntity<?> listAngel() throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "민원 조회"), HttpStatus.OK);
    }

    @ApiParam(value = "시각장애인: 내가 신청한 민원 조회")
    //paging 처리
    @GetMapping(value = "/list/blind")
    public ResponseEntity<?> listBlind() throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "민원 조회"), HttpStatus.OK);
    }

    @ApiParam(value = "민원 상세 조회")
    @GetMapping(value = "/{complaintsSeq")
    public ResponseEntity<?> detailCom() throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "민원 상세 조회"), HttpStatus.OK);
    }

    @ApiParam(value = "민원 반환")
    @PutMapping(value = "/return")
    public ResponseEntity<?> returnCom() throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "반환 완료"), HttpStatus.OK);
    }

    @ApiParam(value = "민원 접수 완료")
    @PutMapping(value = "/submit")
    public ResponseEntity<?> submitCom() throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "민원 접수 완료"), HttpStatus.OK);
    }

    @ApiParam(value = "민원 처리 완료")
    @PutMapping(value = "/complete")
    public ResponseEntity<?> completeCom() throws Exception{
        return new ResponseEntity<>(ResponseFrame.of(HttpStatus.OK, "민원 처리 완료"), HttpStatus.OK);
    }
}
