package com.backend.eyeson.controller;

import com.backend.eyeson.service.FileService;
import com.backend.eyeson.util.ResponseFrame;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("/file")
@Api("FileController")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 파일 S3 서버에 업로드
     *
     * @param req
     * @return Object
     */

    @ApiOperation(value="파일 S3 서버에 업로드",response = Object.class)
    @PostMapping("")
    public ResponseFrame<?> fileChange(@RequestParam("file") MultipartFile req) throws IOException {
        String url = fileService.fileUpload(req);
        ResponseFrame<?> res;

        if(url.length() != 0) {
            res = ResponseFrame.of(url, "파일 S3 서버에 업로드를 성공했습니다.");
        }else{
            res = ResponseFrame.of(url, "파일 S3 서버에 업로드를 실패했습니다.");
        }
        return res;
    }

}
