package com.backend.eyeson.service;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.dto.RequestCompDto;
import com.backend.eyeson.dto.UserDto;
import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.ComplaintsEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.mapper.CompMapper;
import com.backend.eyeson.mapper.StructMapper;
import com.backend.eyeson.mapper.UserMapper;
import com.backend.eyeson.repository.CompRepository;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.util.ReverseGeocoding;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CompService {
    public UserDto getLoginUser() {
        UserEntity user = null;
        try {
            //user = userRepository.findById(SecurityUtil.getCurrentMemberId()).get();
            user = userRepository.findByUserEmail("sa01070@naver.com").get();
        } catch (Exception e) {
            user = new UserEntity();
            user.setUserSeq(9999);
            user.setUserEmail("null@null.com");
            user.setUserGender('M');
            user.setUserFcm("abcdefghijklmnopqrstuvwxyz");
            user.setUserDate(LocalDateTime.now());
        }

        return UserDto.of(user);
    }
    @Autowired
    private final CompRepository compRepository;
    private final UserRepository userRepository;

    public boolean regiserCom(RequestCompDto params) throws IOException {
        ComplaintsEntity complaints = CompMapper.INSTANCE.toEntity(params);
        complaints.setCompAddress(ReverseGeocoding.getAddress(params.getCompAddress()));
        complaints.setCompState(CompStateEnum.PROGRESS_IN);
        System.out.println(getLoginUser().getUserSeq());
        UserEntity user = UserMapper.INSTANCE.toEntity(getLoginUser());

        complaints.setBlindUser(user);
        System.out.println(complaints.getBlindUser().getUserSeq());
        compRepository.save(complaints);
        return true;

    }

    public void listAll(String address) {


    }

    public void listAngel() {

    }

    public void listBlind() {

    }

    public void detailCom() {

    }

    public void returnCom() {

    }

    public void submitCom() {

    }

    public void completeCom() {

    }
}
