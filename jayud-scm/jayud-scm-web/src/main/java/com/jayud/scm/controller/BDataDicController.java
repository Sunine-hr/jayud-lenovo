package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddBDataDicForm;
import com.jayud.scm.model.bo.QueryCustomerForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BDataDic;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.service.IBDataDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

}

