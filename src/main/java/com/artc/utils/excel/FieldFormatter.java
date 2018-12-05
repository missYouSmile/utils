package com.artc.utils.excel;

@FunctionalInterface
public interface FieldFormatter<T, R> {

    R format(T obj);

    class Default implements FieldFormatter<Object, String> {
        @Override
        public String format(Object obj) {
            if (obj == null) {
                return "";
            }
            return obj.toString();
        }
    }
}
