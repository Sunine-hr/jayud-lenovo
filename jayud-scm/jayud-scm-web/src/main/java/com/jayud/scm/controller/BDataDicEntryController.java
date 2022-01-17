package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.BDataDic;
import com.jayud.scm.model.po.BDataDicEntry;
import com.jayud.scm.model.vo.BDataDicEntryVO;
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
 * 数据字典明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/bDataDicEntry")
@Api(tags = "字典数据管理")
public class BDataDicEntryController {

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询字典信息")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {

        IPage<BDataDicEntryVO> page = this.ibDataDicEntryService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);

    }

    @ApiOperation(value = "保存或修改字典信息")
    @PostMapping(value = "/saveOrUpdateBDataDicEntry")
    public CommonResult saveOrUpdateBDataDicEntry(@RequestBody AddBDataDicEntryForm form) {
        if(form.getId() == null){
            BDataDicEntry bDataDicEntry = ibDataDicEntryService.getBDataDicEntryByDicCode(form.getDicCode(),form.getDataValue());
            if(bDataDicEntry != null){
                return CommonResult.error(444,"该数据已存在");
            }
        }
        boolean result = ibDataDicEntryService.saveOrUpdateBDataDicEntry(form);
        if(!result){
            return CommonResult.error(444,"字典数据添加失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id查询字典信息")
    @PostMapping(value = "/getBDataDicEntryId")
    public CommonResult getBDataDicEntryId(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        BDataDicEntryVO bDataDicEntryVO = ibDataDicEntryService.getBDataDicEntryId(id);
        return CommonResult.success(bDataDicEntryVO);
    }

}

