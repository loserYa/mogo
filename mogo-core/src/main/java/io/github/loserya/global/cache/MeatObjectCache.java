package io.github.loserya.global.cache;

import io.github.loserya.module.fill.FieldFillHandler;
import io.github.loserya.module.fill.MetaObjectHandler;
import io.github.loserya.module.fill.entity.FiledFillResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeatObjectCache {

    public static final Map<Class<?>, List<FiledFillResult>> CLASS_LIST_HASH_MAP = new HashMap<>();

    public static final Map<Class<?>, FieldFillHandler<?>> HANDLER_MAP = new HashMap<>();

    public static List<MetaObjectHandler> handlers = new ArrayList<>();

    public static void sorted() {
        handlers = handlers.stream().sorted(Comparator.comparing(MetaObjectHandler::order)).collect(Collectors.toList());
    }

}
