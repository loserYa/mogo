package com.loser.common.entity;


public enum CommonBizStatus implements IBizStatus {

    OK("OK", "SUCCESS"),

    F("F", "FAIL"),

    ;

    private final String code;
    private final String msg;

    CommonBizStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {

        return msg;
    }
}
