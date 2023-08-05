package com.loser.gen.biz;

import com.loser.mysqlCodeGen.gen.handler.ReplaceHandler;
import com.loser.mysqlCodeGen.gen.inner.RunTimeParams;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 替换处理器
 *
 * @author loser
 * @date 2023-01-06  12:20
 */
@AllArgsConstructor
public class ApiReplaceHandler implements ReplaceHandler {

    private Class<?> targetClass;

    @Override
    public List<String> work() {

        List<String> result = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        List<String> templates = new ArrayList<>();
        templates.add("    @ApiModelProperty(value = \"%s\")");
        templates.add("    private %s %s;");
        templates.add("");
        for (Field field : fields) {
            MogoMeta annotation = field.getAnnotation(MogoMeta.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            result.add(String.format(templates.get(0), annotation.value()));
            result.add(String.format(templates.get(1), field.getType().getSimpleName(), field.getName()));
            result.add(templates.get(2));
        }
        return result;

    }

    @Override
    public boolean match(String keyword) {
        return "forApi".equals(keyword);
    }

    @Override
    public void putData(RunTimeParams config) {

        for (Field declaredField : targetClass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Id.class)) {
                config.append("idType", declaredField.getType().getSimpleName());
            }
        }

        MogoMeta annotation = targetClass.getAnnotation(MogoMeta.class);
        config.append("modelDesc", annotation.value()).append("modelName", targetClass.getSimpleName())
                .append("api", targetClass.getSimpleName());
    }

}
