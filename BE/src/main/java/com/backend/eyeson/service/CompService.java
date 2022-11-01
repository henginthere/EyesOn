package com.backend.eyeson.service;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.dto.RequestCompDto;
import com.backend.eyeson.entity.CompStateEnum;
import com.backend.eyeson.entity.ComplaintsEntity;
import com.backend.eyeson.mapper.CompMapper;
import com.backend.eyeson.repository.CompRepository;
import com.backend.eyeson.repository.UserRepository;
import com.backend.eyeson.util.ReverseGeocoding;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class CompService {
    @Autowired
    private final CompRepository compRepository;
    private final UserRepository userRepository;

    public boolean regiserCom(RequestCompDto params) throws IOException {
        ComplaintsEntity complaints = CompMapper.INSTANCE.toEntity(params);
        complaints.setCompAddress(ReverseGeocoding.getAddress(params.getCompAddress()));
        complaints.setCompState(CompStateEnum.PROGRESS_IN);
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
