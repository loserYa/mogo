package io.github.loserya.module.idgen.strategy.impl;

import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;
import io.github.loserya.module.idgen.work.IdWorker;

import java.io.Serializable;

public class SnowStrategy implements IdGenStrategy {

    @Override
    public GEN getType() {
        return GEN.SNOW;
    }

    @Override
    public Serializable genId(Object obj) {
        return IdWorker.getId();
    }

}
