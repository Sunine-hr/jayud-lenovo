package com.jayud.tools.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jayud.common.CommonResult;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.po.TestBean;
import com.jayud.tools.model.vo.CargoNameSmallVO;
import com.jayud.tools.model.vo.CargoNameVO;
import com.jayud.tools.service.ICargoNameService;
import com.jayud.tools.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cargoname")
@Api(tags = "佳裕达小工具-货物名称管理")
public class CargoNameController {

    @Autowired
    ICargoNameService cargoNameService;

    @ApiOperation(value = "导入Excel")
    @RequestMapping(value = "/importExcelV2", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult importExcelV2(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        //读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
//        List<CargoName> list = excelReader.readAll(CargoName.class);
        List<List<Object>> list = excelReader.read();
        cargoNameService.importExcel(list);
        return CommonResult.success("导入Excel成功！");
    }


    @ApiOperation(value = "导出Excel测试")
    @RequestMapping(value = "/exportExcelTest", method = RequestMethod.POST)
    public void exportExcelTest(HttpServletResponse response) throws IOException {

        TestBean bean1 = new TestBean();
        bean1.setName("张三");
        bean1.setAge(22);
        bean1.setPass(true);
        bean1.setScore(66.30);
        bean1.setExamDate(DateUtil.date());

        TestBean bean2 = new TestBean();
        bean2.setName("李四");
        bean2.setAge(28);
        bean2.setPass(false);
        bean2.setScore(38.50);
        bean2.setExamDate(DateUtil.date());

        List<TestBean> rows = CollUtil.newArrayList(bean1, bean2);

        ExcelWriter writer = ExcelUtil.getWriter(true);

//        Map<String, String> map = MapUtil.newHashMap(true); // 重点
//        map.put("name", "姓名");
//        map.put("age", "年龄");
//        map.put("score", "分数");
//        map.put("isPass", "是否通过");
//        map.put("examDate", "考试时间");
//
//        if (map != null) {
//            map.forEach((key, value) -> {
//                writer.addHeaderAlias(key, value);
//            });
//        }


        //自定义标题别名
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("age", "年龄");
        writer.addHeaderAlias("score", "分数");
        writer.addHeaderAlias("isPass", "是否通过");
        writer.addHeaderAlias("examDate", "考试时间");

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(4, "一班成绩单");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows,true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }


    @ApiOperation(value = "导出A类表Excel(A类表:不存在`敏感品名`的货物表)")
    @RequestMapping(value = "/getExportExcelA", method = RequestMethod.GET)
    @ResponseBody
    public void getExportExcelA(HttpServletResponse response) throws IOException {
        List<CargoNameVO> rows = cargoNameService.findCargoNameListByA();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("xh", "序号");
        writer.addHeaderAlias("dh", "袋号");
        writer.addHeaderAlias("dz", "袋重");
        writer.addHeaderAlias("ytdh", "圆通单号");
        writer.addHeaderAlias("tdh", "提单号");
        writer.addHeaderAlias("sl", "数量(PCS)");
        writer.addHeaderAlias("zl", "重量(PCS)");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("hpmc", "货品名称");
        writer.addHeaderAlias("js", "件数");
        writer.addHeaderAlias("pce", "PCE");
        writer.addHeaderAlias("jz", "价值");
        writer.addHeaderAlias("xm1", "姓名1");
        writer.addHeaderAlias("xm2", "姓名2");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("hm1", "号码1");
        writer.addHeaderAlias("xm3", "姓名3");
        writer.addHeaderAlias("hm2", "号码2");
        writer.addHeaderAlias("bjdh", "标记单号");

        Field[] s = CargoNameVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "A类表:不存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("A类表-不存在`敏感品名`的货物表");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "导出A类表Excel(A类表:不存在`敏感品名`的货物表)")
    @RequestMapping(value = "/postExportExcelA", method = RequestMethod.POST)
    @ResponseBody
    public void postExportExcelA(HttpServletResponse response) throws IOException {
        List<CargoNameVO> rows = cargoNameService.findCargoNameListByA();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("xh", "序号");
        writer.addHeaderAlias("dh", "袋号");
        writer.addHeaderAlias("dz", "袋重");
        writer.addHeaderAlias("ytdh", "圆通单号");
        writer.addHeaderAlias("tdh", "提单号");
        writer.addHeaderAlias("sl", "数量(PCS)");
        writer.addHeaderAlias("zl", "重量(PCS)");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("hpmc", "货品名称");
        writer.addHeaderAlias("js", "件数");
        writer.addHeaderAlias("pce", "PCE");
        writer.addHeaderAlias("jz", "价值");
        writer.addHeaderAlias("xm1", "姓名1");
        writer.addHeaderAlias("xm2", "姓名2");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("hm1", "号码1");
        writer.addHeaderAlias("xm3", "姓名3");
        writer.addHeaderAlias("hm2", "号码2");
        writer.addHeaderAlias("bjdh", "标记单号");

        Field[] s = CargoNameVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "A类表:不存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("A类表-不存在`敏感品名`的货物表");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

//        writer.flush(out, true);
        writer.flush(out);

        writer.close();
        IoUtil.close(out);
    }


    @ApiOperation(value = "导出B类表Excel(B类表:存在`敏感品名`的货物表)")
    @RequestMapping(value = "/getExportExcelB", method = RequestMethod.GET)
    @ResponseBody
    public void getExportExcelB(HttpServletResponse response) throws IOException {
        List<CargoNameVO> cargoNameList = cargoNameService.findCargoNameListByB();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("xh", "序号");
        writer.addHeaderAlias("dh", "袋号");
        writer.addHeaderAlias("dz", "袋重");
        writer.addHeaderAlias("ytdh", "圆通单号");
        writer.addHeaderAlias("tdh", "提单号");
        writer.addHeaderAlias("sl", "数量(PCS)");
        writer.addHeaderAlias("zl", "重量(PCS)");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("hpmc", "货品名称");
        writer.addHeaderAlias("js", "件数");
        writer.addHeaderAlias("pce", "PCE");
        writer.addHeaderAlias("jz", "价值");
        writer.addHeaderAlias("xm1", "姓名1");
        writer.addHeaderAlias("xm2", "姓名2");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("hm1", "号码1");
        writer.addHeaderAlias("xm3", "姓名3");
        writer.addHeaderAlias("hm2", "号码2");
        writer.addHeaderAlias("bjdh", "标记单号");

        Field[] s = CargoNameVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "B类表:存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(cargoNameList, true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("B类表-存在`敏感品名`的货物表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);


        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "导出B类表Excel(B类表:存在`敏感品名`的货物表)")
    @RequestMapping(value = "/postExportExcelB", method = RequestMethod.POST)
    @ResponseBody
    public void postExportExcelB(HttpServletResponse response) throws IOException {
        List<CargoNameVO> cargoNameList = cargoNameService.findCargoNameListByB();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("xh", "序号");
        writer.addHeaderAlias("dh", "袋号");
        writer.addHeaderAlias("dz", "袋重");
        writer.addHeaderAlias("ytdh", "圆通单号");
        writer.addHeaderAlias("tdh", "提单号");
        writer.addHeaderAlias("sl", "数量(PCS)");
        writer.addHeaderAlias("zl", "重量(PCS)");
        writer.addHeaderAlias("remark", "备注");
        writer.addHeaderAlias("hpmc", "货品名称");
        writer.addHeaderAlias("js", "件数");
        writer.addHeaderAlias("pce", "PCE");
        writer.addHeaderAlias("jz", "价值");
        writer.addHeaderAlias("xm1", "姓名1");
        writer.addHeaderAlias("xm2", "姓名2");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("hm1", "号码1");
        writer.addHeaderAlias("xm3", "姓名3");
        writer.addHeaderAlias("hm2", "号码2");
        writer.addHeaderAlias("bjdh", "标记单号");

        Field[] s = CargoNameVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "B类表:存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(cargoNameList, true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("B类表-存在`敏感品名`的货物表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "删除所有`货物名称表`")
    @RequestMapping(value = "/deleteAllCargoName", method = RequestMethod.POST)
    public CommonResult deleteAllCargoName(){
        cargoNameService.deleteAllCargoName();
        return CommonResult.success("删除所有`货物名称表`成功");
    }

    //small 小的列
    @ApiOperation(value = "导入Excel,第二版，较少的列")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //配置别名
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("圆通单号","ytdh");
        aliasMap.put("重量","zl");
        aliasMap.put("收货人","xm1");
        aliasMap.put("件数","js");
        aliasMap.put("货品名称","hpmc");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<CargoName> list= excelReader.read(0, 1, CargoName.class);

        cargoNameService.importExcelV2(list);
        return CommonResult.success("导入Excel成功！");
    }


    @ApiOperation(value = "导出A类表Excel(A类表:不存在`敏感品名`的货物表)V2版")
    @RequestMapping(value = "/postExportExcelAV2", method = RequestMethod.GET)
    @ResponseBody
    public void postExportExcelAV2(HttpServletResponse response){
        List<CargoNameSmallVO> rows = cargoNameService.findCargoNameListByAV2();
        ExcelWriter writer = ExcelUtil.getWriter(true);
//        ExcelWriter writer = ExcelUtil.getWriter();

        //自定义标题别名
        writer.addHeaderAlias("ytdh", "圆通单号");
        writer.addHeaderAlias("zl", "重量");
        writer.addHeaderAlias("xm1", "收货人");
        writer.addHeaderAlias("js", "件数");
        writer.addHeaderAlias("hpmc", "货品名称");

        Field[] s = CargoNameSmallVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "A类表:不存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);

        String name = StringUtils.toUtf8String("A类表-不存在`敏感品名`的货物表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
            writer.flush(out);
            writer.close();
            IoUtil.close(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "导出B类表Excel(B类表:存在`敏感品名`的货物表)")
    @RequestMapping(value = "/postExportExcelBV2", method = RequestMethod.GET)
    @ResponseBody
    public void postExportExcelBV2(HttpServletResponse response) throws IOException {
        List<CargoNameSmallVO> cargoNameList = cargoNameService.findCargoNameListByBV2();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("ytdh", "圆通单号");
        writer.addHeaderAlias("zl", "重量");
        writer.addHeaderAlias("xm1", "收货人");
        writer.addHeaderAlias("js", "件数");
        writer.addHeaderAlias("hpmc", "货品名称");

        Field[] s = CargoNameSmallVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "B类表:存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(cargoNameList, true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("B类表-存在`敏感品名`的货物表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "清空`货物名称表`")
    @RequestMapping(value = "/truncateCargoName", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult truncateCargoName(){
        cargoNameService.truncateCargoName();
        return CommonResult.success("清空`货物名称表`成功");
    }


}
