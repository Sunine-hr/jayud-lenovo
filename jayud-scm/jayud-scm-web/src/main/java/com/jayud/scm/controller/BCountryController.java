package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCountryForm;
import com.jayud.scm.model.po.BCountry;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.HsCodeFormVO;
import com.jayud.scm.service.IBCountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 国家表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/bCountry")
@Api(tags = "国家管理")
public class BCountryController {

    @Autowired
    private IBCountryService bCountryService;

    @ApiOperation(value = "国家搜索框")
    @PostMapping(value = "/findCountryList")
    public CommonResult findCountryList(@RequestBody QueryCountryForm form) {
        IPage<BCountryVO> page = this.bCountryService.findCountryList(form);
        CommonPageResult<BCountryVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

