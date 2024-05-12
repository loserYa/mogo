package io.github.loserya.module.idgen.strategy.impl;

import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;
import io.github.loserya.module.idgen.work.IdWorker;

import java.io.Serializable;

public class ULIDStrategy implements IdGenStrategy {
    @Override
    public GEN getType() {
        return GEN.ULID;
    }

    @Override
    public Serializable genId(Object obj) {
        return IdWorker.get26ULID();
    }
}
