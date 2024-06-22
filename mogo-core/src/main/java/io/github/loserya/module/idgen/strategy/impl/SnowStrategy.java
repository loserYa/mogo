package io.github.loserya.module.idgen.strategy.impl;

import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;
import io.github.loserya.module.idgen.work.IdWorker;

public class SnowStrategy implements IdGenStrategy<Long> {

    @Override
    public GEN getType() {
        return GEN.SNOW;
    }

    @Override
    public Long genId(Object obj) {
        return IdWorker.getId();
    }

}
