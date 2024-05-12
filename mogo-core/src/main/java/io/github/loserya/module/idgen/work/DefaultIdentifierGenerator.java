/*
 * Copyright (c) 2011-2023, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.loserya.module.idgen.work;


import java.net.InetAddress;

/**
 * 默认生成器
 *
 * @author JiaChaoYang
 * @reference mp
 * @date 2023/8/11 1:10
 */
public class DefaultIdentifierGenerator implements IdentifierGenerator {

    private final Sequence sequence;

    /**
     * 共享默认单例
     *
     * @author JiaChaoYang
     * @date 2023/8/11 1:10
     */
    @Deprecated
    public DefaultIdentifierGenerator() {
        this.sequence = new Sequence(null);
    }

    public DefaultIdentifierGenerator(InetAddress inetAddress) {
        this.sequence = new Sequence(inetAddress);
    }

    public DefaultIdentifierGenerator(long workerId, long dataCenterId) {
        this.sequence = new Sequence(workerId, dataCenterId);
    }

    public DefaultIdentifierGenerator(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public Long nextId(Object entity) {
        return sequence.nextId();
    }

    public static DefaultIdentifierGenerator getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private static class DefaultInstance {

        public static final DefaultIdentifierGenerator INSTANCE = new DefaultIdentifierGenerator();

    }

}
