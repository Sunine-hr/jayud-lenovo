package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddHgTruckFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.HgTruckFollowVO;
import com.jayud.scm.service.IHgTruckFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 港车跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/hgTruckFollow")
@Api(tags = "港车跟踪信息")
public class HgTruckFollowController {

    @Autowired
    private IHgTruckFollowService hgTruckFollowService;

    @ApiOperation(value = "根据港车id获取港车操作日志")
    @PostMapping(value = "/findListByHgTruckId")
    public CommonResult findListByHgTruckId(@RequestBody QueryCommonForm form) {
        IPage<HgTruckFollowVO> page = this.hgTruckFollowService.findListByHgTruckId(form);
        CommonPageResult<HgTruckFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增操作信息")
    @PostMapping(value = "/addHgTruckFollow")
    public CommonResult addHgTruckFollow(@RequestBody AddHgTruckFollowForm followForm) {
        boolean result = hgTruckFollowService.addHgTruckFollow(followForm);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"跟踪信息添加失败");
    }

}

