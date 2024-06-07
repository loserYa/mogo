package io.github.loserya.core.entity;

import io.github.loserya.hardcode.constant.EUpdateType;

/**
 * 更新字段
 *
 * @author loser
 * @since 1.1.5
 */
public class UpdateField {

    /**
     * 集合对应的列
     */
    private String col;

    /**
     * 更新类型
     */
    private EUpdateType type;

    /**
     * 值
     */
    private Object val;

    public UpdateField() {
    }

    public UpdateField(String col, EUpdateType type, Object val) {
        this.col = col;
        this.type = type;
        this.val = val;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public EUpdateType getType() {
        return type;
    }

    public void setType(EUpdateType type) {
        this.type = type;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }
}
