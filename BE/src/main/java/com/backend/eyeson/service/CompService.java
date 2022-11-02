package com.backend.eyeson.service;

import com.backend.eyeson.dto.*;
import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.ComplaintsEntity;
import com.backend.eyeson.entity.UserEntity;
import com.backend.eyeson.mapper.CompMapper;
import com.backend.eyeson.mapper.StructMapper;
import com.backend.eyeson.mapper.UserMapper;
import com.backend.eyeson.repository.CompRepository;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.util.ReverseGeocoding;
import com.backend.eyeson.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompService {
    public UserDto getLoginUser() {
        UserEntity user = null;
        try {
            //user = userRepository.findById(SecurityUtil.getCurrentMemberSeq()).get();
            user = userRepository.findByUserEmail("sa01070@gmail.com").get();
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

    public boolean registerCom(RequestCompDto params) throws IOException {
        ComplaintsEntity complaints = CompMapper.INSTANCE.toEntity(params);
        complaints.setCompAddress(ReverseGeocoding.getAddress(params.getCompAddress()));
        complaints.setCompState(CompStateEnum.PROGRESS_IN);
        UserEntity user = UserMapper.INSTANCE.toEntity(getLoginUser());

        complaints.setBlindUser(user);
        compRepository.save(complaints);
        return true;

    }

    public PagingResult<ComplaintsDto> listAll(Pageable pageable) {
        Page<ComplaintsEntity> complaintsPage = null;
        complaintsPage = compRepository.findAllByCompStateOrderByCompSeqAsc(CompStateEnum.PROGRESS_IN, pageable);
        List<ComplaintsDto> compList = new ArrayList<>();

        for(ComplaintsEntity complaintsEntity: complaintsPage) {
            compList.add(new ComplaintsDto(complaintsEntity));
        }
        PagingResult result =new PagingResult<ComplaintsDto>(pageable.getPageNumber(), complaintsPage.getTotalPages() -1, compList);
        return result;

    }

    public PagingResult<ComplaintsDto> listAngel(Pageable pageable) {
        Page<ComplaintsEntity> complaintsPage = null;
        UserEntity loginUser = UserMapper.INSTANCE.toEntity(getLoginUser());
        complaintsPage = compRepository.findAllByAngelUserOrderByCompSeqAsc(loginUser, pageable);
        List<ComplaintsDto> compList = new ArrayList<>();

        for(ComplaintsEntity complaintsEntity: complaintsPage) {
            compList.add(new ComplaintsDto(complaintsEntity));
        }
        PagingResult result =new PagingResult<ComplaintsDto>(pageable.getPageNumber(), complaintsPage.getTotalPages() -1, compList);
        return result;

    }

    public PagingResult<ComplaintsDto> listBlind(Pageable pageable) {
        Page<ComplaintsEntity> complaintsPage = null;
        UserEntity loginUser = UserMapper.INSTANCE.toEntity(getLoginUser());
        complaintsPage = compRepository.findAllByBlindUserOrderByCompSeqAsc(loginUser, pageable);
        List<ComplaintsDto> compList = new ArrayList<>();

        for(ComplaintsEntity complaintsEntity: complaintsPage) {
            compList.add(new ComplaintsDto(complaintsEntity));
        }
        PagingResult result =new PagingResult<ComplaintsDto>(pageable.getPageNumber(), complaintsPage.getTotalPages() -1, compList);
        return result;

    }

    public ResponseCompDto detailCom(long compSeq) {
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        ResponseCompDto complaintsDto = CompMapper.INSTANCE.toDto(complaintsEntity);
        return complaintsDto;

    }

    public ResponseCompDto returnCom(RequestCompDto requestCompDto) {
        long compSeq = requestCompDto.getCompSeq();
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        complaintsEntity.setCompReturn(requestCompDto.getCompReturn());
        complaintsEntity.setCompState(CompStateEnum.RETURN);
        complaintsEntity.setAngelUser(UserMapper.INSTANCE.toEntity(getLoginUser()));
        compRepository.save(complaintsEntity);
        ResponseCompDto result = CompMapper.INSTANCE.toDto(complaintsEntity);
        return result;

    }

    public ResponseCompDto submitCom(RequestCompDto requestCompDto) {
        long compSeq = requestCompDto.getCompSeq();
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        complaintsEntity.setCompTitle(requestCompDto.getCompTitle());
        complaintsEntity.setCompState(CompStateEnum.REGIST_DONE);
        complaintsEntity.setAngelUser(UserMapper.INSTANCE.toEntity(getLoginUser()));
        compRepository.save(complaintsEntity);
        ResponseCompDto result = CompMapper.INSTANCE.toDto(complaintsEntity);
        return result;

    }

    public ResponseCompDto completeCom(RequestCompDto requestCompDto) {
        long compSeq = requestCompDto.getCompSeq();
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        complaintsEntity.setCompResultContent(requestCompDto.getCompResultContent());
        complaintsEntity.setCompState(CompStateEnum.PROGRESS_DONE);
        complaintsEntity.setAngelUser(UserMapper.INSTANCE.toEntity(getLoginUser()));
        compRepository.save(complaintsEntity);
        ResponseCompDto result = CompMapper.INSTANCE.toDto(complaintsEntity);
        return result;

    }
}
