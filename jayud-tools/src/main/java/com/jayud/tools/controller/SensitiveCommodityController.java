package com.jayud.tools.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.bo.SensitiveCommodityForm;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.model.po.TestBean;
import com.jayud.tools.model.vo.SensitiveCommodityVO;
import com.jayud.tools.service.ISensitiveCommodityService;
import com.jayud.tools.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/sensitivecommodity")
@Api(tags = "佳裕达小工具-敏感品名管理")
public class SensitiveCommodityController {

    @Autowired
    ISensitiveCommodityService sensitiveCommodityService;

    @ApiOperation(value = "查询敏感品名list")
    @PostMapping(value = "/getSensitiveCommodityList")
    public CommonResult<List<SensitiveCommodity>> getSensitiveCommodityList(@RequestBody QuerySensitiveCommodityForm form){
        List<SensitiveCommodity> userList = sensitiveCommodityService.getSensitiveCommodityList(form);
        return CommonResult.success(userList);
    }

    @ApiOperation(value = "保存`敏感品名`（新增或修改）")
    @PostMapping(value = "/saveSensitiveCommodity")
    public CommonResult saveSensitiveCommodity(@Valid @RequestBody SensitiveCommodityForm sensitiveCommodityForm){
        return sensitiveCommodityService.saveSensitiveCommodity(sensitiveCommodityForm);
    }

    @ApiOperation(value = "删除`敏感品名`")
    @PostMapping(value = "/deleteSensitiveCommodityById")
    public CommonResult deleteSensitiveCommodityById(@RequestParam(value = "id") Long id){
        sensitiveCommodityService.deleteSensitiveCommodityById(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "敏感品名分页查询")
    @PostMapping(value = "/findSensitiveCommodityByPage")
    public CommonResult<CommonPageResult<SensitiveCommodityVO>> findSensitiveCommodityByPage(@RequestBody QuerySensitiveCommodityForm  form){
        IPage<SensitiveCommodityVO> pageList = sensitiveCommodityService.findSensitiveCommodityByPage(form);
        CommonPageResult<SensitiveCommodityVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


    @ApiOperation(value = "下载敏感品名导入模板")
    @RequestMapping(value = "/exportExcelTemplate", method = RequestMethod.GET)
    public void exportExcelTest(HttpServletResponse response) throws IOException {
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("品名", "品名A");
        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("品名", "品名B");
        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows,true);
        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("敏感品名导入模板");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "导入敏感品名数据")
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
        aliasMap.put("品名","name");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<SensitiveCommodity> list= excelReader.read(0, 1, SensitiveCommodity.class);

        sensitiveCommodityService.importExcelV2(list);
        return CommonResult.success("导入Excel成功！");
    }
}
