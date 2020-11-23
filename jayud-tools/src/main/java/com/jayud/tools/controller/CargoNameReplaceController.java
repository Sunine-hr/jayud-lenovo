package com.jayud.tools.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.jayud.common.CommonResult;
import com.jayud.tools.model.bo.CargoNameReplaceForm;
import com.jayud.tools.model.po.CargoNameReplace;
import com.jayud.tools.service.ICargoNameReplaceService;
import com.jayud.tools.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/cargonamereplace")
@Api(tags = "货物名称替换接口")
@ApiSort(value = 3)
public class CargoNameReplaceController {

    @Autowired
    ICargoNameReplaceService cargoNameReplaceService;

    @ApiOperation(value = "查询敏感品名list")
    @ApiOperationSupport(order = 1)
    @PostMapping(value = "/findCargoNameReplace")
    public CommonResult<List<CargoNameReplace>> findCargoNameReplace(@RequestBody CargoNameReplaceForm form){
        List<CargoNameReplace> userList = cargoNameReplaceService.findCargoNameReplace(form);
        return CommonResult.success(userList);
    }

    @ApiOperation(value = "下载货物名称替换表模板")
    @ApiOperationSupport(order = 2)
    @RequestMapping(value = "/exportExcelTemplate", method = RequestMethod.GET)
    public void exportExcelTest(HttpServletResponse response) throws IOException {
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("货品名称", "服饰1");
        row1.put("替换名称", "衣服1");
        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("货品名称", "服饰2");
        row2.put("替换名称", "衣服2");
        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows,true);
        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("货物名称替换表模板");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "导入货物名称替换表")
    @ApiOperationSupport(order = 3)
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
        aliasMap.put("货品名称","hpmc");
        aliasMap.put("替换名称","replaceName");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<CargoNameReplace> list= excelReader.read(0, 1, CargoNameReplace.class);

        cargoNameReplaceService.importExcel(list);
        return CommonResult.success("导入Excel成功！");
    }

    @ApiOperation(value = "保存`货物名称替换表`（新增或修改）")
    @ApiOperationSupport(order = 4)
    @PostMapping(value = "/saveCargoNameReplace")
    public CommonResult saveCargoNameReplace(@Valid @RequestBody CargoNameReplaceForm form){
        return cargoNameReplaceService.saveCargoNameReplace(form);
    }


    @ApiOperation(value = "删除`货物名称替换表`")
    @ApiOperationSupport(order = 5)
    @PostMapping(value = "/deleteCargoNameReplace")
    public CommonResult deleteCargoNameReplace(@RequestBody CargoNameReplaceForm form){
        Long id = form.getId();
        if(id == null){
            return CommonResult.error(-1, "删除时id不能为空");
        }
        return cargoNameReplaceService.deleteCargoNameReplace(id);
    }

}
