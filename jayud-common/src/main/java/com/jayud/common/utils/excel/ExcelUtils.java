//package com.jayud.common.utils.excel;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.poi.excel.ExcelReader;
//import cn.hutool.poi.excel.ExcelUtil;
//import cn.hutool.poi.excel.ExcelWriter;
//import com.jayud.common.dto.SheetDTO;
//import com.jayud.common.enums.ResultEnum;
//import com.jayud.common.exception.Asserts;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.streaming.SXSSFSheet;
//import org.springframework.lang.NonNull;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * Hutool-Excel工具类封装
// *
// * @author william
// * @description
// * @Date: 2020-07-31 15:55
// */
//@Slf4j
//public class ExcelUtils {
//
//    /**
//     * 根据请求的excel输出对应的接收实体列表,并同时进行重复行校验
//     * <br> 注意：
//     * <li> 要校验重复行的属性需要标注@NotDuplicate注解
//     * <li> 此方法只抓取excel第一页
//     * <li> excel的第一行必须为标题
//     * <li> excel标题名与实体名称必须匹配至少一个，否则匹配失败返回null
//     *
//     * @param file 请求入参
//     * @param clz  接受数据的实体类
//     * @param <T>
//     * @return
//     */
//    public static <T> List<T> getExcelInfoThenCheck(MultipartFile file, Class<T> clz, Integer sheetIndex) {
//        List<T> excelInfo = getExcelInfo(file, clz, sheetIndex);
//        String errorStr = Check4List.getNullOrDuplicateErrorString(excelInfo);
//        if (!errorStr.isEmpty()) {
//            Asserts.fail(ResultEnum.PARAM_ERROR, errorStr);
//        }
//        return excelInfo;
//    }
//
//
//    public static <T> List<T> getExcelInfo(MultipartFile file, Class<T> clz, Integer sheetIndex) {
//        //判定是否继承自ExcelReaderParent父类
//        if (!ExcelPageBase.class.isAssignableFrom(clz)) {
//            return doGetSimpleExcelInfo(file, clz, sheetIndex);
//        } else {
//            return doGetClassicExcelInfo(file, clz, sheetIndex);
//        }
//    }
//
//
//    public static <T extends ExcelPageBase> List<T> getCustomizedExcelInfo(MultipartFile file, T parent, Integer sheetIndex) {
//        try {
//            //直接读出数据，等待处理
//            ExcelReader reader = ExcelUtil.getReader(file.getInputStream(), sheetIndex);
//            List<List<Object>> readResult = reader.read();
//
//            if (readResult.isEmpty()) {
//                return null;
//            }
//
//
//            //将读出的excel数据装载为实体列表
//            List<T> results = new ArrayList<>();
//            //遍历行
//            for (int i = parent.getStartRow() - 1; i < readResult.size(); i++) {
//                //每一行一个新实例
//                Class<T> clz = (Class<T>) parent.getClass();
//                T result = (T) clz.newInstance();
//                List<Object> items = readResult.get(i);
//
//                //获取需要的字段名-列号,直接尝试赋值
//                Arrays.stream(clz.getDeclaredFields())
//                        .filter(e -> {
//                            ColProperties annotation = e.getAnnotation(ColProperties.class);
//                            return (null != annotation && annotation.col() > 0);
//                        })//筛出需要的字段
//                        .forEach(e -> {
//                            int col = e.getAnnotation(ColProperties.class).col();
//                            Class<?> type = e.getType();
//
//                            Method setMethod = null;
//                            try {
//                                setMethod = clz.getMethod("set" + e.getName().substring(0, 1).toUpperCase() + e.getName().substring(1), type);
//                            } catch (NoSuchMethodException noSuchMethodException) {
////                                noSuchMethodException.printStackTrace();
//                                log.error(noSuchMethodException.getMessage());
//                            }
//
//                            Object colValue = null;
//                            try {
//                                colValue = items.get(col - 1);
//                            } catch (Exception exception) {
////                                exception.printStackTrace();
//                               log.error(exception.getMessage());
//                            }
//
//                            if (null == setMethod || null == colValue) {
//                                return;
//                            }
//
//                            try {
//                                if (Objects.equals(type, colValue.getClass())) {
//                                    //一致直接赋值完事
//                                    setMethod.invoke(result, colValue);
//                                } else {
//                                    //如果不巧遇到excel类型与实体属性类型不一致，尝试匹配为实体属性类型，搞不定就抛异常
//                                    if (type == Integer.class) {
//                                        setMethod.invoke(result, Integer.parseInt(colValue.toString()));
//                                    } else if (type == BigDecimal.class) {
//                                        setMethod.invoke(result, new BigDecimal(colValue.toString()));
//                                    } else if (type == String.class) {
//                                        setMethod.invoke(result, colValue.toString());
//                                    } else if (type == Boolean.class) {
//                                        setMethod.invoke(result, Boolean.parseBoolean(colValue.toString()));
//                                    }
//                                }
//                            } catch (Exception ex) {
////                                ex.printStackTrace();
//                                log.error(ex.getMessage());
//                            }
//                        });
//
//                results.add(result);
//            }
//            return results;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return null;
//
//    }
//
//
//    private static <T> List<T> doGetClassicExcelInfo(MultipartFile file, Class<T> clz, Integer sheetIndex) {
//        //todo 带起始位置并且有备注名的数据读取
//        return null;
//    }
//
//
//    /**
//     * 根据请求的excel输出对应的接收实体列表
//     * <br> 注意：
//     * <li> 此方法只抓取excel的第[sheetIndex]页
//     * <li> excel的第一行必须为标题
//     * <li> excel标题名与实体名称必须匹配至少一个，否则匹配失败返回null
//     *
//     * @param file 请求入参
//     * @param clz  接受数据的实体类
//     * @param <T>
//     * @return
//     */
//    @Deprecated
//    private static <T> List<T> doGetSimpleExcelInfo(MultipartFile file, Class<T> clz, Integer sheetIndex) {
//        try {
//            ExcelReader reader = ExcelUtil.getReader(file.getInputStream(), sheetIndex);
//            List<List<Object>> readResult = reader.read(0);
//            if (readResult.isEmpty()) {
//                return null;
//            }
//            //获取字段名列表names
//            List<String> names = readResult.get(0).stream().map(e -> {
//                return e.toString();
//            }).collect(Collectors.toList());
//
//            //返回数据类泛型的新实例，并获取对应的属性列表
//            T result = clz.newInstance();
//            Field[] declaredFields = clz.getDeclaredFields();
//
//            //将读出的excel数据装载为实体列表
//            List<T> results = new ArrayList<>();
//            //遍历行
//            for (int i = 1; i < readResult.size(); i++) {
//                //遍历列
//                for (int j = 0; j < names.size(); j++) {
//                    //当前列名
//                    String name = names.get(j);
//                    //当前单元格数据
//                    //根据实体尝试抓取时可能遇到excel没有数据，抓取列时包IndexOutofBound异常，这里做一下处理，跳过
//                    Object obj = null;
//                    try {
//                        obj = readResult.get(i).get(j);
//                    } catch (IndexOutOfBoundsException e) {
//                        log.debug("当前单元格：(" + i + "," + j + ")无数据");
//                    }
//
//
//                    //检查此字段在实体中是否有对应set方法，如有，尝试赋值，如无或赋值失败以及各种bug，抛出异常并继续循环
//                    try {
//                        checkAndSetProperty(clz, result, declaredFields, name, obj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        continue;
//                    }
//                }
//                results.add(result);
//            }
//            return results;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * @param clz            对象数据类
//     * @param result         对象数据类新实例
//     * @param declaredFields 属性列表
//     * @param name           当前单元格字段名称
//     * @param obj            当前单元格字段值
//     * @param <T>            对象数据类泛型
//     * @throws NoSuchMethodException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    @Deprecated
//    private static <T> void checkAndSetProperty(Class<T> clz, T result, Field[] declaredFields, String name, Object obj) throws Exception {
//        //校验属性中是否有set方法与字段名匹配，如果匹配则给该属性赋值
//        for (Field declaredField : declaredFields) {
//            if (Objects.equals(declaredField.getName(), name)) {
//                //如果属性名匹配到了excel字段名，尝试获取set方法并赋值
//                Class<?> type = declaredField.getType();
//                Method setMethod = clz.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), type);
//                //excel读出的数据类型与实体属性是否一致
//                if (Objects.equals(type, obj.getClass())) {
//                    //一致直接赋值完事
//                    setMethod.invoke(result, obj);
//                } else {
//                    //如果不巧遇到excel类型与实体属性类型不一致，尝试匹配为实体属性类型，搞不定就抛异常
//                    if (type == Integer.class) {
//                        setMethod.invoke(result, Integer.parseInt(obj.toString()));
//                    } else if (type == BigDecimal.class) {
//                        setMethod.invoke(result, new BigDecimal(obj.toString()));
//                    } else if (type == String.class) {
//                        setMethod.invoke(result, obj.toString());
//                    } else if (type == Boolean.class) {
//                        setMethod.invoke(result, Boolean.parseBoolean(obj.toString()));
//                    }
//                }
//            }
//        }
//    }
//
////    private static <T> T checkAndSetProperty(Class<T> clz, Object obj) throws Exception {
////        //校验属性中是否有set方法与字段名匹配，如果匹配则给该属性赋值
////        for (Field declaredField : declaredFields) {
////            if (Objects.equals(declaredField.getName(), name)) {
////                //如果属性名匹配到了excel字段名，尝试获取set方法并赋值
////                Class<?> type = declaredField.getType();
////                Method setMethod = clz.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), type);
////                //excel读出的数据类型与实体属性是否一致
////                if (Objects.equals(type, obj.getClass())) {
////                    //一致直接赋值完事
////                    setMethod.invoke(result, obj);
////                } else {
////                    //如果不巧遇到excel类型与实体属性类型不一致，尝试匹配为实体属性类型，搞不定就抛异常
////                    if (type == Integer.class) {
////                        setMethod.invoke(result, Integer.parseInt(obj.toString()));
////                    } else if (type == BigDecimal.class) {
////                        setMethod.invoke(result, new BigDecimal(obj.toString()));
////                    } else if (type == String.class) {
////                        setMethod.invoke(result, obj.toString());
////                    } else if (type == Boolean.class) {
////                        setMethod.invoke(result, Boolean.parseBoolean(obj.toString()));
////                    }
////                }
////            }
////        }
////    }
//
//    /**
//     * 输出单页带数据的表格：数据不能为空
//     *
//     * @param fileName
//     * @param rows
//     * @param response
//     * @param <T>
//     */
//    public static <T> void exportSinglePageExcel(@NonNull String fileName, List<T> rows, HttpServletResponse response) {
//        OutputStream outputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            fileName = fileName + ".xls";
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
//            ExcelWriter bigWriter = ExcelUtil.getBigWriter();
//            //标题获取
//            if (rows == null || rows.isEmpty()) {
//                return;
//            }
//            //装载标题
//            T t = rows.get(0);
//            Class<?> clz = t.getClass();
//            Field[] fields = clz.getDeclaredFields();
//            List<String> titleName = new ArrayList<>();
//            for (Field field : fields) {
//                titleName.add(field.getName());
//                bigWriter.addHeaderAlias(field.getName(), field.getName());
//            }
//            //标题写入
//            bigWriter.writeHeadRow(titleName);
//            //数据写入
//            bigWriter.write(rows);
////            bigWriter.autoSizeColumnAll();
//            bigWriter.flush(outputStream);
//            bigWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    log.error("关闭导出excel流异常:{}", e, e.getMessage());
//                }
//            }
//        }
//    }
//
//    /**
//     * 输出单页空表格模板,不带数据
//     *
//     * @param
//     * @param response
//     * @param <T>
//     */
//    public static <T> void exportSinglePageExcel(String fileName, Class<T> clz, HttpServletResponse response) {
//        OutputStream outputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            fileName = fileName + ".xls";
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
//            ExcelWriter bigWriter = ExcelUtil.getBigWriter();
//            //装载标题
//            T t = clz.newInstance();
//            Field[] fields = clz.getDeclaredFields();
//            List<String> titleName = new ArrayList<>();
//            for (Field field : fields) {
//                titleName.add(field.getName());
//                bigWriter.addHeaderAlias(field.getName(), field.getName());
//            }
//            //标题写入
//            bigWriter.writeHeadRow(titleName);
//            bigWriter.flush(outputStream);
//            bigWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    log.error("关闭导出excel流异常:{}", e, e.getMessage());
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 导出多页excel
//     *
//     * @param sheets
//     * @param fileName
//     * @param response
//     * @param <T>
//     */
//    public static <T> void exportMultiPageExcel(List<SheetDTO> sheets, String fileName, HttpServletResponse response) {
//        OutputStream outputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            fileName = fileName + ".xls";
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        if (CollectionUtil.isEmpty(sheets)) {
//            log.error("没有传入任何页，无法输出！");
//            return;
//        }
//        ExcelWriter bigWriter = ExcelUtil.getBigWriter();
//
//        for (int i = 0; i < sheets.size(); i++) {
//            SheetDTO sheet = sheets.get(i);
//            if (i == 0) {
//                bigWriter.renameSheet(0, sheet.getSheetName());
//            } else {
//                bigWriter.setSheet(sheet.getSheetName());
//            }
//            //装载字段名
//            Class<T> clz = sheet.getClz();
//            Field[] fields = clz.getDeclaredFields();
//            List<String> titleName = new ArrayList<>();
//            for (Field field : fields) {
//                ColProperties colProperties = field.getAnnotation(ColProperties.class);
//                if (null != colProperties && !sheet.getExcludeCols().contains(field.getName())) {
//                    titleName.add(field.getName());
//                    if (StringUtils.isNotEmpty(colProperties.name())) {
//                        //标题的别名 addHeaderAlias( [字段名],[别名] )
//                        bigWriter.addHeaderAlias(field.getName(), colProperties.name());
//                    } else {
//                        bigWriter.addHeaderAlias(field.getName(), field.getName());
//                    }
//                }
//
//
//            }
//            //只写出有名字的列
//            bigWriter.setOnlyAlias(true);
//            //标题写入(write强制输出标题并设定alias后将不用手动写入标题，只用预装载alias即可)
//            //bigWriter.writeHeadRow(titleName);
//            //数据写入
//            if (CollectionUtil.isNotEmpty(sheet.getData())) {
////                List<T> dataRows = sheet.getData();
//                bigWriter.write(sheet.getData(), true);
//            }
//        }
//
//
//        List<Sheet> currentSheets = bigWriter.getSheets();
//        for (Sheet currentSheetOrigin : currentSheets) {
//            SXSSFSheet currentSheet = (SXSSFSheet) currentSheetOrigin;
//            currentSheet.trackAllColumnsForAutoSizing();
//            int columnCount = bigWriter.getColumnCount();
//            for (int j = 0; j < columnCount; j++) {
//                // 调整每一列宽度
//                currentSheet.autoSizeColumn((short) j);
//                // 解决自动设置列宽中文失效的问题
//                currentSheet.setColumnWidth(j, currentSheet.getColumnWidth(j) * 17 / 10);
//            }
//        }
//
//        try {
//            bigWriter.flush(outputStream, true);
//            bigWriter.close();
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
