package com.loser.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询的列
 *
 * @author loser
 * @date 2023-02-04  17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectField {

    /**
     * 集合对应的列
     */
    private String col;

}
