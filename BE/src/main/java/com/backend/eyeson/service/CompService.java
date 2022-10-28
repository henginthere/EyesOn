package com.backend.eyeson.service;

import com.backend.eyeson.repository.CompRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompService {
    private final CompRepository compRepository;
}
