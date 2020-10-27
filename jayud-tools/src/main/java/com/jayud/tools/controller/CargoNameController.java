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
import com.jayud.tools.service.ICargoNameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/cargoname")
@Api(tags = "佳裕达小工具-货物名称管理")
public class CargoNameController {

    @Autowired
    ICargoNameService cargoNameService;

    @ApiOperation(value = "导入Excel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public CommonResult importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request){

//        ExcelReader reader = ExcelUtil.getReader("d:/test.xlsx");
//        List<List<Object>> readAll = reader.read();

//        ExcelReader reader = ExcelUtil.getReader("d:/test.xlsx");
//        List<Map<String,Object>> readAll = reader.readAll();

        ExcelReader reader = ExcelUtil.getReader("d:/test.xlsx");
        List<TestBean> all = reader.readAll(TestBean.class);

        System.out.println("aaaaaaaa");

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
        List<CargoName> list = excelReader.readAll(CargoName.class);

        List<List<Object>> readAll = excelReader.read();

//
//        cargoNameService.importExcel(list);
        return CommonResult.success("导入Excel成功！");
    }


    @ApiOperation(value = "导入Excel")
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    public CommonResult export(HttpServletResponse response) throws IOException {

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

        //自定义标题别名
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("age", "年龄");
        writer.addHeaderAlias("score", "分数");
        writer.addHeaderAlias("isPass", "是否通过");
        writer.addHeaderAlias("examDate", "考试时间");

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(4, "一班成绩单");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);

        //out为OutputStream，需要写出到的目标流

        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        ServletOutputStream out=response.getOutputStream();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xlsx");

        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
        return CommonResult.success("导出成功！");
    }

}
