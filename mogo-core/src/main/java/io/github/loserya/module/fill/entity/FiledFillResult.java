package io.github.loserya.module.fill.entity;

import io.github.loserya.module.fill.FieldFillHandler;
import io.github.loserya.module.fill.hardcode.FieldFill;

import java.lang.reflect.Field;

public class FiledFillResult {

    private Field field;

    private FieldFill fieldFill;

    private Class<? extends FieldFillHandler<?>> handlerClazz;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public FieldFill getFieldFill() {
        return fieldFill;
    }

    public void setFieldFill(FieldFill fieldFill) {
        this.fieldFill = fieldFill;
    }

    public Class<? extends FieldFillHandler<?>> getHandlerClazz() {
        return handlerClazz;
    }

    public void setHandlerClazz(Class<? extends FieldFillHandler<?>> handlerClazz) {
        this.handlerClazz = handlerClazz;
    }
}
