package io.github.loserya.core.entity;


/**
 * 查询的列
 *
 * @author loser
 * @date 2023-02-04  17:07
 */
public class SelectField {

    public SelectField() {
    }

    public SelectField(String col) {
        this.col = col;
    }

    /**
     * 集合对应的列
     */
    private String col;

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }
}
