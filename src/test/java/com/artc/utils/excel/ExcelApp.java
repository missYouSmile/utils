package com.artc.utils.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.artc.utils.pojo.Person;

public class ExcelApp {

    public static void main(String[] args) throws Exception {
        List<Person> data = new ArrayList<>();
        data.add(Person.builder()
                       .id(1L)
                       .name("张三")
                       .height(1.78d)
                       .weight(70)
                       .birthday(new Date())
                       .build());

        Workbook workbook = ExcelHelper.createXSSF("1", data);
        try (OutputStream output = new FileOutputStream(new File("C:\\tmp\\test-xssf.xlsx"))) {
            workbook.write(output);
        }

        data.add(Person.builder()
                       .id(2L)
                       .name("李四")
                       .height(1.71d)
                       .weight(62)
                       .birthday(new Date())
                       .build());

        Workbook wb = ExcelHelper.createXSSF(data);
        try (OutputStream output = new FileOutputStream(new File("C:\\tmp\\test-xssf-default.xlsx"))) {
            wb.write(output);
        }
    }

}
