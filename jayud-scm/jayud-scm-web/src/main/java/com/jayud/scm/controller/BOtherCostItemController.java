package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddOtherCostForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.model.vo.BOtherCostItemVO;
import com.jayud.scm.service.IBOtherCostItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 费用名称表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@RestController
@RequestMapping("/bOtherCostItem")
@Api(tags = "费用名称管理")
public class BOtherCostItemController {

    @Autowired
    private IBOtherCostItemService ibOtherCostItemService;

    @ApiOperation(value = "分页查询费用名称")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<BOtherCostItemVO>> findByPage(@RequestBody QueryCommonForm form){
        IPage<BOtherCostItemVO> page = this.ibOtherCostItemService.findByPage(form);
        CommonPageResult<BOtherCostItemVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

