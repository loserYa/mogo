package com.loser.ext;

public class Condition {

    public Condition() {
    }

    public Condition(String field, ECheckType type, Object val) {
        this.field = field;
        this.type = type;
        this.val = val;
    }

    private String field;

    private ECheckType type = ECheckType.NOTNULL;

    private Object val;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ECheckType getType() {
        return type;
    }

    public void setType(ECheckType type) {
        this.type = type;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }
}
