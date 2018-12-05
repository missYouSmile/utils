package com.artc.utils.pojo;

import java.io.Serializable;
import java.util.Date;

import com.artc.utils.excel.ColumnField;
import com.artc.utils.excel.DateFieldFormatter;
import com.artc.utils.excel.DateFieldParser;
import com.artc.utils.excel.FieldParser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    @ColumnField(value = 0, title = "编号", parser = FieldParser.LongParser.class)
    private Long id;

    @ColumnField(value = 1, title = "姓名")
    private String name;

    @ColumnField(value = 2, title = "身高", parser = FieldParser.DoubleParser.class)
    private double height;

    @ColumnField(value = 3, title = "体重", parser = FieldParser.DoubleParser.class)
    private double weight;

    @ColumnField(value = 4, title = "生日", formatter = DateFieldFormatter.class, parser = DateFieldParser.class)
    private Date birthday;
}
