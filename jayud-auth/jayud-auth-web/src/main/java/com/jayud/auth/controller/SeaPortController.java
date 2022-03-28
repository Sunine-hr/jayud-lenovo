package com.jayud.auth.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.AddSeaPortForm;
import com.jayud.auth.model.bo.QuerySeaPortForm;
import com.jayud.auth.model.po.SeaPort;
import com.jayud.auth.model.po.SysDict;
import com.jayud.auth.model.vo.SeaPortVO;
import com.jayud.auth.service.ISeaPortService;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    public BaseResult findByPage(@RequestBody QuerySeaPortForm form) {
        IPage<SeaPortVO> page = seaPortService.findByPage(form);

        return BaseResult.ok(page);
    }

    @ApiOperation("增加或修改港口信息")
    @PostMapping("/saveOrUpdateSeaPort")
    public BaseResult saveOrUpdateSeaPort(@RequestBody AddSeaPortForm form) {
        //判断代码是否存在，判断名称是否存在
        SeaPort seaPort = seaPortService.isCodeExistence(form.getCode());
        SeaPort seaPort1 = seaPortService.isNameExistence(form.getName());
        if(form.getId() != null){

            if(seaPort != null && !seaPort.getId().equals(form.getId())){
                return BaseResult.error(444,"港口代码已存在");
            }
            if(seaPort1 != null && !seaPort1.getId().equals(form.getId())){
                return BaseResult.error(444,"港口名称已存在");
            }
        }else{
            if(seaPort != null){
                return BaseResult.error(444,"港口代码已存在");
            }
            if(seaPort1 != null){
                return BaseResult.error(444,"港口名称已存在");
            }
        }
        boolean flag = seaPortService.saveOrUpdateSeaPort(form);
        if(!flag){
            return BaseResult.error(444,"添加港口失败");
        }
        return BaseResult.ok();
    }

    @ApiOperation("删除港口信息")
    @PostMapping("/deleteSeaPort")
    public BaseResult deleteSeaPort(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        boolean flag = seaPortService.deleteSeaPort(id);
        if(!flag){
            return BaseResult.error(444,"删除港口失败");
        }
        return BaseResult.ok();
    }

    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SeaPort>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SeaPort> queryById(@RequestParam(name = "id", required = true) int id) {
        SeaPort seaPort = seaPortService.getById(id);
        return BaseResult.ok(seaPort);
    }

    @ApiOperation("读取excel中的数据，添加到数据库中")
    @PostMapping("/importSeaPort")
    public CommonResult importSeaPort(@RequestBody MultipartFile file) {
        InputStream is = null;
        List<AddSeaPortForm> list = new ArrayList<>();
        try {
            is = file.getInputStream();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            AddSeaPortForm bean = null;

            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // 循环行Row
                for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                        bean = new AddSeaPortForm();
                        XSSFCell route = hssfRow.getCell(1);
                        XSSFCell code = hssfRow.getCell(2);
                        XSSFCell name = hssfRow.getCell(3);
                        XSSFCell chineseName = hssfRow.getCell(4);
                        XSSFCell country = hssfRow.getCell(5);

                        route.setCellType(CellType.STRING);
                        code.setCellType(CellType.STRING);
                        name.setCellType(CellType.STRING);
                        chineseName.setCellType(CellType.STRING);
                        country.setCellType(CellType.STRING);

                        bean.setName(name.getRichStringCellValue().getString());
                        bean.setRoute(route.getRichStringCellValue().getString());
                        bean.setCode(code.getRichStringCellValue().getString());
                        bean.setChineseName(chineseName.getRichStringCellValue().getString());
                        bean.setCountry(country.getRichStringCellValue().getString());
                        list.add(bean);
                    }
                }
            }
            is.close();
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

