package com.artc.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnField {

    /**
     * 字段填充的列编号
     */
    int value();

    /**
     * 标题
     */
    String title() default "";

    /**
     * 格式化
     */
    Class<? extends FieldFormatter<?, ?>> formatter() default FieldFormatter.Default.class;

    /**
     * 解析器
     */
    Class<? extends FieldParser<?>> parser() default FieldParser.Default.class;
}
