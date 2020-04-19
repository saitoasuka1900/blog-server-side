package com.example.demo.util.result;

public enum ResultCode {
    /*
    成功
     */
    SUCCESS(200),
    /*
    登录超时
     */
    LOGIN_TIMEOUT(400),
    /*
    验证错误（token或rnd错误或者验证码错误）
     */
    AUTHORIZED_FAILED(401),
    /*
    服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500),
    /*
    其余错误
     */
    OTHER(404);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
