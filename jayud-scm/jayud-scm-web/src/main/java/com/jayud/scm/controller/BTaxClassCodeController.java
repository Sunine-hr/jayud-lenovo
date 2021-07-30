package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.enums.CorrespondEnum;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.BTaxClassCodeVO;
import com.jayud.scm.model.vo.HsCodeFormVO;
import com.jayud.scm.service.IBTaxClassCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 税务分类表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-29
 */
@RestController
@RequestMapping("/bTaxClassCode")
@Api(tags = "税务分类管理")
public class BTaxClassCodeController {

    @Autowired
    private IBTaxClassCodeService ibTaxClassCodeService;

    @ApiOperation(value = "海关编码列表")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@Valid @RequestBody QueryForm form) {

        if(form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null){
            return CommonResult.error(444,"该条件无法搜索");
        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

        IPage<BTaxClassCodeVO> page = this.ibTaxClassCodeService.findByPage(form);
        CommonPageResult<BTaxClassCodeVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

