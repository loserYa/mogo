package com.loser.hardcode.constant;


public enum ExecuteMethodEnum {

    GET_ONE("getOne"),
    SAVE("save"),
    SAVE_BATCH("saveBatch"),
    REMOVE_BY_ID("removeById"),
    REMOVE("remove"),
    UPDATE_BY_ID("updateById"),
    UPDATE("update"),
    GET_BY_ID("getById"),
    LIST_BY_IDS("listByIds"),
    COUNT("count"),
    LIST("list"),
    PAGE("page"),
    EXIST("exist"),
    ;

    private final String method;

    ExecuteMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
