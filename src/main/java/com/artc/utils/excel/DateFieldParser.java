package com.artc.utils.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldParser implements FieldParser<Date> {

    private final ThreadLocal<SimpleDateFormat> formatHolder = new ThreadLocal<>();

    @Override
    public Date parse(String value) {
        if (formatHolder.get() == null) {
            formatHolder.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
        try {
            return formatHolder.get().parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("时间转化异常: value " + value, e);
        }
    }
}
