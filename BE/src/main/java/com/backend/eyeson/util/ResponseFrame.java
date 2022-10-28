package com.backend.eyeson.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFrame<T> {
    private HttpStatus status;
    private T data;
    private String message;

    // 반환 데이터 없을 때
    public static ResponseFrame<?> of(HttpStatus status, String message){
        ResponseFrame<?> frame = new ResponseFrame<>();
        frame.setData(null);
        frame.setMessage(message);
        frame.setStatus(status);

        return frame;
    }

    // 반환 데이터 있을 때
    public static <T> ResponseFrame<T> of(T data, String message){
        ResponseFrame<T> frame = new ResponseFrame<>();
        frame.setStatus(HttpStatus.OK);
        frame.setData(data);
        frame.setMessage(message);

        return frame;
    }

}
