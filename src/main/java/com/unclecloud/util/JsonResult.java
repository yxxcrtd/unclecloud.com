package com.unclecloud.util;

import lombok.Data;

import static com.unclecloud.util.Constants.INT_200;
import static com.unclecloud.util.Constants.INT_MINUS_200;

@Data
public class JsonResult<T> {

    private int code;

    private String message;

    private T data;

    public static final <T> JsonResult<T> jsonResultSuccess(String message, T t) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setCode(INT_200);
        jsonResult.setMessage(message);
        jsonResult.setData(t);
        return jsonResult;
    }

    public static final <T> JsonResult<T> jsonResultFail(String message) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setCode(INT_MINUS_200);
        jsonResult.setMessage(message);
        return jsonResult;
    }

}
