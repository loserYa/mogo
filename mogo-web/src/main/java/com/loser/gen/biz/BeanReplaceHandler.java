package com.loser.gen.biz;

import com.loser.mysqlCodeGen.gen.handler.ReplaceHandler;
import com.loser.mysqlCodeGen.gen.inner.RunTimeParams;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BeanReplaceHandler implements ReplaceHandler {

    private Class<?> targetClass;

    @Override
    public List<String> work() {

        List<String> result = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        List<String> templates = new ArrayList<>();
        templates.add("    /**");
        templates.add("     * %s");
        templates.add("     */");
        templates.add("    @Id");
        templates.add("    private %s %s;");
        templates.add("");
        for (Field field : fields) {
            MogoMeta annotation = field.getAnnotation(MogoMeta.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            result.add(templates.get(0));
            result.add(String.format(templates.get(1), annotation.value()));
            result.add(templates.get(2));
            Id id = field.getAnnotation(Id.class);
            if (Objects.nonNull(id)) {
                result.add(templates.get(3));
            }
            result.add(String.format(templates.get(4), field.getType().getSimpleName(), field.getName()));
            result.add(templates.get(5));
        }
        return result;

    }

    @Override
    public boolean match(String keyword) {
        return "forModel".equals(keyword);
    }

    @Override
    public void putData(RunTimeParams config) {
        MogoMeta annotation = targetClass.getAnnotation(MogoMeta.class);
        config.append("modelDesc", annotation.value()).append("modelName", targetClass.getSimpleName()).append("api", targetClass.getSimpleName());
    }
}
