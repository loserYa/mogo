package com.loser.core.entity;

import com.loser.core.constant.ESortType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序字段
 *
 * @author loser
 * @date 2023-02-04  17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortCondition {

    /**
     * 排序类型
     */
    private ESortType sortType = ESortType.ASC;

    /**
     * 集合对应的列
     */
    private String col;

}
