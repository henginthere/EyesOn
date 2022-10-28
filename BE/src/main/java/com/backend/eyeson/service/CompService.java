package com.backend.eyeson.service;

import com.backend.eyeson.dto.ComplaintsDto;
import com.backend.eyeson.entity.ComplaintsEntity;
import com.backend.eyeson.repository.CompRepository;
import com.backend.eyeson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompService {
    @Autowired
    private final CompRepository compRepository;
    private final UserRepository userRepository;

    public void regiserCom(ComplaintsDto params) {

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
