package com.loser.core.entity;

import com.loser.core.constant.ESortType;

/**
 * 排序字段
 *
 * @author loser
 * @date 2023-02-04  17:07
 */
public class SortCondition {

    public SortCondition() {
    }

    public SortCondition(ESortType sortType, String col) {
        this.sortType = sortType;
        this.col = col;
    }

    /**
     * 排序类型
     */
    private ESortType sortType = ESortType.ASC;

    /**
     * 集合对应的列
     */
    private String col;

    public ESortType getSortType() {
        return sortType;
    }

    public void setSortType(ESortType sortType) {
        this.sortType = sortType;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }
}
