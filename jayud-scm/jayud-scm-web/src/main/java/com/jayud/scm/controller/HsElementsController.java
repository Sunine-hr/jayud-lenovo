package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddHsCodeForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HsElements;
import com.jayud.scm.model.vo.CodeElementsVO;
import com.jayud.scm.service.IHsElementsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 申报要素表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/hsElements")
@Api(tags = "申报要素管理")
public class HsElementsController {

    @Autowired
    private IHsElementsService hsElementsService;

    @ApiOperation(value = "查询所有申报要素")
    @PostMapping(value = "/findElements")
    public CommonResult findElements(@RequestBody QueryCommonForm form) {

        IPage<CodeElementsVO> page = hsElementsService.findElements(form);
        CommonPageResult pageResult = new CommonPageResult(page);
        return CommonResult.success(pageResult);
    }

}

