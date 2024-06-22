package io.github.loserya.module.idgen.strategy.impl;

import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.module.idgen.entity.SysAutoIDCount;
import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Objects;

public class AutoStrategy implements IdGenStrategy<Long> {

    @Override
    public GEN getType() {
        return GEN.AUTO;
    }

    @Override
    public Long genId(Object obj) {

        MongoTemplate mongoTemplate = MongoTemplateCache.getMongoTemplate();
        String name = obj.getClass().getName();
        // 构建查询条件
        Query query = new Query(Criteria.where("_id").is(name));
        // 构建更新操作
        Update update = new Update().inc("maxId", 1);
        // 执行更新操作
        SysAutoIDCount modify = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), SysAutoIDCount.class);
        return Objects.isNull(modify) ? 1 : modify.getMaxId();

    }

}
