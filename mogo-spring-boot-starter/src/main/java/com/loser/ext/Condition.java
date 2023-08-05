package com.loser.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

    private String field;

    private ECheckType type = ECheckType.NOTNULL;

    private Object val;

}
