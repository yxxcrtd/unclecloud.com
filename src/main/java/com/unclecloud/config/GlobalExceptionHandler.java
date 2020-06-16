package com.unclecloud.config;

import com.unclecloud.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static com.unclecloud.util.JsonResult.jsonResultFail;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    JsonResult defaultErrorHandler(Exception e) {
        return jsonResultFail("出错了！");
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    JsonResult fileUploadExceptionHandler(MaxUploadSizeExceededException e) {
        return jsonResultFail("文件大小不能超过2M");
    }

}
