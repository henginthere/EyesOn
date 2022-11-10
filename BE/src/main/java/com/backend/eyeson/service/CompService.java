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
import org.springframework.web.multipart.MultipartFile;

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
            user = userRepository.findById(SecurityUtil.getCurrentMemberSeq()).get();
//            user = userRepository.findByUserEmail("sa01070@gmail.com").get();
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
    private final FileService fileService;
    private final FirebaseService firebaseService;

    public boolean registerCom(RequestCompDto params, MultipartFile multipartFile) throws IOException {
        ComplaintsEntity complaints = CompMapper.INSTANCE.toEntity(params);
        complaints.setCompAddress(ReverseGeocoding.getAddress(params.getCompAddress()));
        complaints.setCompState(CompStateEnum.PROGRESS_IN);

        UserEntity user = UserMapper.INSTANCE.toEntity(getLoginUser());
        String url = fileService.fileUpload(multipartFile);
        complaints.setCompImage(multipartFile.getOriginalFilename());
        complaints.setBlindUser(user);
        compRepository.save(complaints);

        return true;
    }

    public PagingResult<ComplaintsDto> listAll(Pageable pageable) {
        Page<ComplaintsEntity> complaintsPage = null;
        complaintsPage = compRepository.findAllByCompStateOrderByCompSeqAsc(CompStateEnum.PROGRESS_IN, pageable);
        List<ResponseCompDto> compList = new ArrayList<>();

        for(ComplaintsEntity complaintsEntity: complaintsPage) {
            compList.add(CompMapper.INSTANCE.toDto(complaintsEntity));
        }

        PagingResult result =new PagingResult<ResponseCompDto>(pageable.getPageNumber(), complaintsPage.getTotalPages() -1, compList);

        return result;
    }

    public PagingResult<ResponseCompDto> listAngel(Pageable pageable) {
        Page<ComplaintsEntity> complaintsPage = null;
        UserEntity loginUser = UserMapper.INSTANCE.toEntity(getLoginUser());
        complaintsPage = compRepository.findAllByAngelUserOrderByCompSeqAsc(loginUser, pageable);
        List<ResponseCompDto> compList = new ArrayList<>();

        for(ComplaintsEntity complaintsEntity: complaintsPage) {
            compList.add(CompMapper.INSTANCE.toDto(complaintsEntity));
        }

        PagingResult result =new PagingResult<ResponseCompDto>(pageable.getPageNumber(), complaintsPage.getTotalPages() -1, compList);

        return result;
    }

    public PagingResult<ResponseCompDto> listBlind(Pageable pageable) {
        Page<ComplaintsEntity> complaintsPage = null;
        UserEntity loginUser = UserMapper.INSTANCE.toEntity(getLoginUser());
        complaintsPage = compRepository.findAllByBlindUserOrderByCompSeqAsc(loginUser, pageable);
        List<ResponseCompDto> compList = new ArrayList<>();

        for(ComplaintsEntity complaintsEntity: complaintsPage) {
            compList.add(CompMapper.INSTANCE.toDto(complaintsEntity));
        }

        PagingResult result =new PagingResult<ResponseCompDto>(pageable.getPageNumber(), complaintsPage.getTotalPages() -1, compList);

        return result;

    }

    public ResponseCompDto detailCom(long compSeq) {
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        ResponseCompDto complaintsDto = CompMapper.INSTANCE.toDto(complaintsEntity);

        return complaintsDto;
    }

    public ResponseCompDto returnCom(RequestCompDto requestCompDto) throws IOException{
        long compSeq = requestCompDto.getCompSeq();
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        complaintsEntity.setCompReturn(requestCompDto.getCompReturn());
        complaintsEntity.setCompState(CompStateEnum.RETURN);
        complaintsEntity.setAngelUser(UserMapper.INSTANCE.toEntity(getLoginUser()));

        compRepository.save(complaintsEntity);
        ResponseCompDto result = CompMapper.INSTANCE.toDto(complaintsEntity);

        //알림보내기
        String title = "신청한 민원이 반환됐습니다.";
        String body = requestCompDto.getCompReturn();
        String fcmToken = complaintsEntity.getBlindUser().getUserFcm();
        firebaseService.sendMessageTo(fcmToken, title, body);

        return result;
    }

    public ResponseCompDto submitCom(RequestCompDto requestCompDto) throws IOException{
        long compSeq = requestCompDto.getCompSeq();
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        complaintsEntity.setCompTitle(requestCompDto.getCompTitle());
        complaintsEntity.setCompState(CompStateEnum.REGIST_DONE);
        complaintsEntity.setAngelUser(UserMapper.INSTANCE.toEntity(getLoginUser()));

        compRepository.save(complaintsEntity);
        ResponseCompDto result = CompMapper.INSTANCE.toDto(complaintsEntity);

        //알림보내기
        String title = "신청한 민원이 접수됐습니다.";
        String body = requestCompDto.getCompTitle();
        String fcmToken = complaintsEntity.getBlindUser().getUserFcm();
        firebaseService.sendMessageTo(fcmToken, title, body);

        return result;
    }

    public ResponseCompDto completeCom(RequestCompDto requestCompDto) throws IOException{
        long compSeq = requestCompDto.getCompSeq();
        ComplaintsEntity complaintsEntity = compRepository.findByCompSeq(compSeq).get();
        complaintsEntity.setCompResultContent(requestCompDto.getCompResultContent());
        complaintsEntity.setCompState(CompStateEnum.PROGRESS_DONE);
        complaintsEntity.setAngelUser(UserMapper.INSTANCE.toEntity(getLoginUser()));

        compRepository.save(complaintsEntity);
        ResponseCompDto result = CompMapper.INSTANCE.toDto(complaintsEntity);

        //알림보내기
        String title = "신청한 민원이 처리완료됐습니다.";
        String body = requestCompDto.getCompResultContent();
        String fcmToken = complaintsEntity.getBlindUser().getUserFcm();
        firebaseService.sendMessageTo(fcmToken, title, body);

        return result;
    }
}
