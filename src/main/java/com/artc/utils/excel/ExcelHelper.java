package com.artc.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel 文档操作帮助类
 */
public class ExcelHelper {

    /**
     * 创建一个excel对象, 并填充内容 , excel 类型 {@link XSSFWorkbook}
     *
     * @param sheetName sheet 名称
     * @param data      数据
     */
    public static Workbook createXSSF(String sheetName, Collection<?> data) {
        return create(sheetName, data, XSSFWorkbook.class);
    }

    /**
     * 创建一个excel对象, 并填充内容 , excel 类型 {@link XSSFWorkbook}
     *
     * @param data 数据
     */
    public static Workbook createXSSF(Collection<?> data) {
        return create(null, data, XSSFWorkbook.class);
    }

    /**
     * 读取 第 1个sheet 从 第 2行 开始读取
     */
    public static <T> List<T> read(String fileName, Class<T> clazz) {
        return read(fileName, 1, clazz);
    }

    /**
     * 读取 第 1个sheet
     *
     * @param startRow 开始行号
     */
    public static <T> List<T> read(String fileName, int startRow, Class<T> clazz) {
        return read(fileName, 0, startRow, clazz);
    }

    /**
     * 读取excel 文件
     *
     * @param fileName   文件名
     * @param sheetIndex sheet 索引 从 0 开始
     * @param startRow   开始读取的行数
     * @param <T>        期望结果类型
     */
    public static <T> List<T> read(String fileName, int sheetIndex, int startRow, Class<T> clazz) {
        try (FileInputStream fis = new FileInputStream(new File(fileName))) {
            Workbook workbook = new XSSFWorkbook(fis);
            return read(workbook, sheetIndex, startRow, clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件不存在: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取工作簿
     *
     * @param workbook   工作簿
     * @param sheetIndex sheet索引号
     * @param startRow   开始行号
     */
    public static <T> List<T> read(Workbook workbook, int sheetIndex, int startRow, Class<T> clazz) {
        try {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            int rows = sheet.getLastRowNum() + 1;
            int rowIndex = startRow;

            List<Field> fields = fieldList(clazz);

            Map<Field, FieldParser<Object>> parserMap = collectFieldParserMap(fields);

            T entity;
            List<T> list = new ArrayList<>();
            while (rowIndex < rows) {
                Row row = sheet.getRow(rowIndex++);
                int columns = row.getLastCellNum();
                int columnIndex = 0;
                entity = clazz.newInstance();
                while (columnIndex < columns && columnIndex < fields.size()) {
                    Cell cell = row.getCell(columnIndex);
                    String value = cell.getStringCellValue();
                    Field field = fields.get(columnIndex);
                    field.set(entity, parserMap.get(field).parse(value));
                    columnIndex++;
                }
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("数据填充异常:", e);
        }
    }

    /**
     * 创建表格
     *
     * @param data         数据
     * @param workbookType 表格类型
     */
    public static Workbook create(String sheetName, Collection<?> data, Class<? extends Workbook> workbookType) {
        Workbook workbook = create(workbookType);
        sheetName = sheetName == null ? "sheet" : sheetName;
        Sheet sheet = workbook.createSheet(sheetName);

        if (isEmpty(data)) {
            return workbook;
        }

        // 获取字段信息
        Object[] dataArray = data.toArray();
        List<Field> fl = fieldList(dataArray[0].getClass());

        int rowNum = 0;

        // 填充 title
        fullTitle(workbook, sheet, fl, rowNum);
        rowNum++;

        // 填充表格数据
        fullData(dataArray, sheet, fl, rowNum);

        return workbook;
    }

    /**
     * 填充数据
     *
     * @param dataArray   数据数组
     * @param fl          字段列表
     * @param startRowNum 开始填充行号
     */
    private static void fullData(Object[] dataArray, Sheet sheet, List<Field> fl, int startRowNum) {

        Map<Field, FieldFormatter<Object, String>> formatterMap = collectFieldFormatterMap(fl);

        for (Object obj : dataArray) {
            try {
                Row row = sheet.createRow(startRowNum++);
                for (int j = 0; j < fl.size(); j++) {
                    Field field = fl.get(j);
                    Object value = field.get(obj);
                    row.createCell(j).setCellValue(formatterMap.get(field).format(value));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("字段访问异常", e);
            }
        }
    }

    /**
     * 获取字段转换器
     */
    private static Map<Field, FieldFormatter<Object, String>> collectFieldFormatterMap(List<Field> fl) {
        Map<Field, FieldFormatter<Object, String>> formatterMap = new HashMap<>();
        for (Field field : fl) {
            try {
                formatterMap.put(field, (FieldFormatter<Object, String>) field.getAnnotation(ColumnField.class).formatter().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("字段格式化器初始化异常", e);
            }
        }
        return formatterMap;
    }

    /**
     * 获取字段解析器
     */
    private static Map<Field, FieldParser<Object>> collectFieldParserMap(List<Field> fl) {
        Map<Field, FieldParser<Object>> formatterMap = new HashMap<>();
        for (Field field : fl) {
            try {
                formatterMap.put(field, (FieldParser<Object>) field.getAnnotation(ColumnField.class).parser().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("字段格式化器初始化异常", e);
            }
        }
        return formatterMap;
    }

    /**
     * 填充表头
     *
     * @param fl     字段列表
     * @param rowNum 行号
     */
    private static void fullTitle(Workbook workbook, Sheet sheet, List<Field> fl, int rowNum) {
        CellStyle titleStyle = createDefaultTitleStyle(workbook);
        Row titleRow = sheet.createRow(rowNum);
        for (int i = 0; i < fl.size(); i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(fl.get(i).getAnnotation(ColumnField.class).title());
        }
    }

    /**
     * 获取字段列表, 并排序
     */
    private static List<Field> fieldList(Class<?> clazz) {

        Field[] fields = clazz.getDeclaredFields();
        List<Field> fl = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ColumnField.class)) {
                field.setAccessible(true);
                fl.add(field);
            }
        }

        fl.sort(Comparator.comparingInt(a -> a.getAnnotation(ColumnField.class).value()));

        return fl;
    }

    /**
     * 创建默认的 title 样式
     *
     * @param workbook 表
     */
    private static CellStyle createDefaultTitleStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); // 字体大小
        font.setColor(Font.COLOR_NORMAL); // 字体颜色
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 字体粗细
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 对齐方式
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建表格
     */
    private static Workbook create(Class<? extends Workbook> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("workbook init failed!", e);
        }
    }

    private static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
