package io.github.loserya.module.logic.entity;

/**
 * 逻辑删除配置
 *
 * @author loser
 * @since 1.0.0
 */
public class LogicProperty {


    /**
     * 逻辑删除全局属性名
     */
    private String logicDeleteField = "logicDel";

    /**
     * 逻辑删除全局值（默认 1、表示已删除）
     */
    private String logicDeleteValue = "1";

    /**
     * 逻辑未删除全局值（默认 0、表示未删除）
     */
    private String logicNotDeleteValue = "0";

    public String getLogicDeleteField() {
        return logicDeleteField;
    }

    public void setLogicDeleteField(String logicDeleteField) {
        this.logicDeleteField = logicDeleteField;
    }

    public String getLogicDeleteValue() {
        return logicDeleteValue;
    }

    public void setLogicDeleteValue(String logicDeleteValue) {
        this.logicDeleteValue = logicDeleteValue;
    }

    public String getLogicNotDeleteValue() {
        return logicNotDeleteValue;
    }

    public void setLogicNotDeleteValue(String logicNotDeleteValue) {
        this.logicNotDeleteValue = logicNotDeleteValue;
    }
}
