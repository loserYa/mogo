package io.github.loserya.module.idgen.strategy;

import io.github.loserya.module.idgen.hardcode.GEN;

import java.io.Serializable;

public interface IdGenStrategy {

    GEN getType();

    Serializable genId(Object obj);

}
