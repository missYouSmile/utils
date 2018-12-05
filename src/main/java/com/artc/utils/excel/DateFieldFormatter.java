package com.artc.utils.excel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldFormatter implements FieldFormatter<Date, String> {

    private final ThreadLocal<SimpleDateFormat> formatHolder = new ThreadLocal<>();

    @Override
    public String format(Date obj) {
        if (formatHolder.get() == null) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            formatHolder.set(new SimpleDateFormat(pattern));
        }
        return formatHolder.get().format(obj);
    }

}
