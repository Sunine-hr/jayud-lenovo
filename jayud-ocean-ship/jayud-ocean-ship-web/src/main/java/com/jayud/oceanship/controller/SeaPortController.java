package com.jayud.oceanship.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.oceanship.bo.AddSeaPortForm;
import com.jayud.oceanship.bo.QuerySeaPortForm;
import com.jayud.oceanship.po.SeaPort;
import com.jayud.oceanship.service.ISeaPortService;
import com.jayud.oceanship.vo.SeaPortVO;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static rx.internal.operators.NotificationLite.getValue;

/**
 * <p>
 * 船港口地址表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
@RestController
@RequestMapping("/seaPort")
public class SeaPortController {

    @Autowired
    private ISeaPortService seaPortService;

    @ApiOperation("分页查询港口列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QuerySeaPortForm form) {
        IPage<SeaPortVO> page = seaPortService.findByPage(form);

        return CommonResult.success(page);
    }

    @ApiOperation("增加或修改港口信息")
    @PostMapping("/saveOrUpdateSeaPort")
    public CommonResult saveOrUpdateSeaPort(@RequestBody AddSeaPortForm form) {
        if(form.getId() != null){
            //判断代码是否存在，判断名称是否存在
            SeaPort seaPort = seaPortService.isCodeExistence(form.getCode());
            SeaPort seaPort1 = seaPortService.isNameExistence(form.getName());
            if(seaPort != null && seaPort.getId().equals(form.getId())){
                return CommonResult.error(444,"港口代码已存在");
            }
            if(seaPort1 != null && seaPort1.getId().equals(form.getId())){
                return CommonResult.error(444,"港口名称已存在");
            }
        }else{
            if(form.getCode() == null){
                return CommonResult.error(444,"港口代码不能为空");
            }
            if(form.getName() == null){
                return CommonResult.error(444,"港口名称不能为空");
            }
        }
        boolean flag = seaPortService.saveOrUpdateSeaPort(form);
        if(!flag){
            return CommonResult.error(444,"添加港口失败");
        }
        return CommonResult.success();
    }

    @ApiOperation("删除港口信息")
    @PostMapping("/deleteSeaPort")
    public CommonResult deleteSeaPort(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        boolean flag = seaPortService.deleteSeaPort(id);
        if(!flag){
            return CommonResult.error(444,"删除港口失败");
        }
        return CommonResult.success();
    }

    @ApiOperation("读取excel中的数据，添加到数据库中")
    @PostMapping("/importSeaPort")
    public CommonResult importSeaPort(@RequestBody MultipartFile file) {
        InputStream is = null;
        List<AddSeaPortForm> list = new ArrayList<>();
        try {
            is = file.getInputStream();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            AddSeaPortForm bean = null;

            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // 循环行Row
                for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                        bean = new AddSeaPortForm();
                        HSSFCell route = hssfRow.getCell(0);
                        HSSFCell code = hssfRow.getCell(1);
                        HSSFCell name = hssfRow.getCell(2);
                        HSSFCell chineseName = hssfRow.getCell(3);
                        HSSFCell country = hssfRow.getCell(4);

                        bean.setName(getValue(name));
                        bean.setRoute(getValue(route));
                        bean.setCode(getValue(code));
                        bean.setChineseName(getValue(chineseName));
                        bean.setCountry(getValue(country));
                        list.add(bean);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean flag = seaPortService.saveOrUpdateBatchSeaPort(list);
        if(!flag){
            return CommonResult.error(444,"导入港口失败");
        }
        return CommonResult.success();
    }
}

