package io.github.loserya.aop.interceptor;

import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.module.logic.IgnoreLogic;
import io.github.loserya.module.transaction.MogoTransaction;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * mogo aop 参数
 *
 * @author loser
 * @since 1.1.8
 */
public class MogoAopParams {

    private MogoAopParams() {
    }

    private MogoDsParams mogoDsParams;

    private MogoTsParams mogoTsParams;

    private MogoIgnoreLogicParams mogoIgnoreLogicParams;

    /**
     * 使用构建者 构建复杂对象
     */
    public static class Builder {

        private final MogoDsParams mogoDsParams = new MogoDsParams();
        private final MogoTsParams mogoTsParams = new MogoTsParams();
        private final MogoIgnoreLogicParams mogoIgnoreLogicParams = new MogoIgnoreLogicParams();

        public MogoAopParams build() {
            MogoAopParams mogoAopParams = new MogoAopParams();
            mogoAopParams.setMogoDsParams(mogoDsParams);
            mogoAopParams.setMogoTsParams(mogoTsParams);
            mogoAopParams.setMogoIgnoreLogicParams(mogoIgnoreLogicParams);
            return mogoAopParams;
        }

        public Builder classAnnoDs(MongoDs classAnno) {
            mogoDsParams.classAnno = classAnno;
            return this;
        }

        public Builder classAnnoIgnore(IgnoreLogic classAnno) {
            mogoIgnoreLogicParams.classAnno = classAnno;
            return this;
        }

        public Builder classAnnoTs(MogoTransaction classAnno) {
            mogoTsParams.classAnno = classAnno;
            return this;
        }

        public Builder methodMapperDs(Map<Method, MongoDs> methodMapper) {
            mogoDsParams.methodMapper = methodMapper;
            return this;
        }

        public Builder methodMapperTs(Map<Method, MogoTransaction> methodMapper) {
            mogoTsParams.methodMapper = methodMapper;
            return this;
        }

        public Builder methodMapperIgnore(Map<Method, IgnoreLogic> methodMapper) {
            mogoIgnoreLogicParams.methodMapper = methodMapper;
            return this;
        }

    }

    public static class MogoDsParams {

        private MogoDsParams() {
        }

        private MongoDs classAnno;
        private Map<Method, MongoDs> methodMapper = new HashMap<>();

        public MongoDs getClassAnno() {
            return classAnno;
        }

        public Map<Method, MongoDs> getMethodMapper() {
            return methodMapper;
        }

    }

    public static class MogoTsParams {

        private MogoTsParams() {
        }

        private MogoTransaction classAnno;
        private Map<Method, MogoTransaction> methodMapper = new HashMap<>();

        public MogoTransaction getClassAnno() {
            return classAnno;
        }

        public Map<Method, MogoTransaction> getMethodMapper() {
            return methodMapper;
        }

    }

    public static class MogoIgnoreLogicParams {

        private MogoIgnoreLogicParams() {
        }

        private IgnoreLogic classAnno;
        private Map<Method, IgnoreLogic> methodMapper = new HashMap<>();

        public IgnoreLogic getClassAnno() {
            return classAnno;
        }

        public Map<Method, IgnoreLogic> getMethodMapper() {
            return methodMapper;
        }
    }

    public MogoDsParams getMogoDsParams() {
        return mogoDsParams;
    }

    public void setMogoDsParams(MogoDsParams mogoDsParams) {
        this.mogoDsParams = mogoDsParams;
    }

    public MogoTsParams getMogoTsParams() {
        return mogoTsParams;
    }

    public void setMogoTsParams(MogoTsParams mogoTsParams) {
        this.mogoTsParams = mogoTsParams;
    }

    public MogoIgnoreLogicParams getMogoIgnoreLogicParams() {
        return mogoIgnoreLogicParams;
    }

    public void setMogoIgnoreLogicParams(MogoIgnoreLogicParams mogoIgnoreLogicParams) {
        this.mogoIgnoreLogicParams = mogoIgnoreLogicParams;
    }


}
