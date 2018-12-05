package com.artc.utils.excel;

import java.util.List;

import com.artc.utils.pojo.Person;

public class ExcelReadApp {

    public static void main(String[] args) {
        List<Person> list = ExcelHelper.read("C:\\tmp\\test-xssf-default.xlsx",
                                             0, 1, Person.class);
        list.forEach(System.out::println);
        System.out.println();
        list = ExcelHelper.read("C:\\tmp\\test-xssf.xlsx", Person.class);
        list.forEach(System.out::println);
    }

}
