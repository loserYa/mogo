package io.github.loserya.module.idgen.strategy.impl;

import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;
import io.github.loserya.module.idgen.work.IdWorker;

public class ULIDStrategy implements IdGenStrategy<String> {
    @Override
    public GEN getType() {
        return GEN.ULID;
    }

    @Override
    public String genId(Object obj) {
        return IdWorker.get26ULID();
    }
}
