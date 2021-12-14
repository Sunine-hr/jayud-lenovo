package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.BDataDic;
import com.jayud.scm.model.po.BDataDicEntry;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.IBDataDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/bDataDic")
@Api(tags = "字典主表管理")
public class BDataDicController {

    @Autowired
    private IBDataDicService ibDataDicService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询字典信息")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryForm form) {

        IPage<BDataDicVO> page = this.ibDataDicService.findByPage(form);
        CommonPageResult<BDataDicVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);

    }

    @ApiOperation(value = "保存或修改字典信息")
    @PostMapping(value = "/saveOrUpdateBDataDic")
    public CommonResult saveOrUpdateBDataDic(@RequestBody AddBDataDicForm form) {
        if(form.getId() == null){
            BDataDic bDataDic = ibDataDicService.getBDataDicByDicCode(form.getDicCode());
            if(bDataDic != null){
                return CommonResult.error(444,"该配置编码已存在");
            }
        }
        boolean result = ibDataDicService.saveOrUpdateBDataDic(form);
        if(!result){
            return CommonResult.error(444,"字典数据添加失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id查询字典信息")
    @PostMapping(value = "/getBDataDicById")
    public CommonResult getBDataDicById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        BDataDicVO bDataDicVO = ibDataDicService.getBDataDicById(id);
        return CommonResult.success(bDataDicVO);
    }

    @ApiOperation(value = "上传文件-导入基础资料明细信息")
    @RequestMapping(value = "/importExcelByBDataDicEntry", method = RequestMethod.POST)
    @ApiOperationSupport(order = 4)
    public CommonResult importExcelByBDataDicEntry(@RequestParam("file") MultipartFile file, @RequestParam(value = "id") Integer id){
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }

//        Integer id = MapUtil.getInt(map, "id");
        if(id == null){
            return CommonResult.error(444,"请选择导入的字典类型数据");
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
        aliasMap.put("代码","dataValue");
        aliasMap.put("名称","dataText");
        aliasMap.put("备注","remark");
        aliasMap.put("备注1","reserved1");
        aliasMap.put("备注2","reserved2");
        aliasMap.put("备注3","reserved3");
        aliasMap.put("备注4","reserved4");
        aliasMap.put("备注5","reserved5");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<AddBDataDicEntryForm> list = excelReader.read(0, 1, AddBDataDicEntryForm.class);
        BDataDic bDataDic = this.ibDataDicService.getById(id);
        for (AddBDataDicEntryForm addBDataDicEntryForm : list) {
            addBDataDicEntryForm.setDicId(bDataDic.getId().toString());
            addBDataDicEntryForm.setDicCode(bDataDic.getDicCode());
            addBDataDicEntryForm.setDicDesc(bDataDic.getDicDesc());
        }
        List<BDataDicEntry> bDataDicEntries = ConvertUtil.convertList(list, BDataDicEntry.class);
        boolean result = this.ibDataDicEntryService.saveBatch(bDataDicEntries);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"导入失败");
    }

}

