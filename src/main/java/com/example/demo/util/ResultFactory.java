package com.example.demo.util;

import com.example.demo.util.result.Result;
import com.example.demo.util.result.ResultCode;

public class ResultFactory {

    public static Result buildSuccessResult(Object data) {
        return buildResult(ResultCode.SUCCESS, "成功", data);
    }

    public static Result buildLoginTimeOutResult(Object data) {
        return buildResult(ResultCode.LOGIN_TIMEOUT, "登录超时", data);
    }

    public static Result buildAuthorizedFailedResult(Object data) {
        return buildResult(ResultCode.AUTHORIZED_FAILED, "验证失败", data);
    }

    public static Result buildInternalServerErrorResult(Object data) {
        return buildResult(ResultCode.INTERNAL_SERVER_ERROR, "服务器错误", data);
    }

    public static Result buildFailedResult(String resultMessage, Object resultData) {
        return buildResult(ResultCode.OTHER, resultMessage, resultData);
    }

    public static Result buildResult(ResultCode resultCode, String resultMessage, Object resultData) {
        return new Result(resultCode.code, resultMessage, resultData);
    }
}
