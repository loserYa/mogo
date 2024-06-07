package io.github.loserya.hardcode.constant;


public enum ExecuteMethodEnum {

    GET_ONE("getOne"),
    SAVE("save"),
    SAVE_BATCH("saveBatch"),
    REMOVE("remove"),
    UPDATE("update"),
    LAMBDA_UPDATE("lambdaUpdate"),
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
