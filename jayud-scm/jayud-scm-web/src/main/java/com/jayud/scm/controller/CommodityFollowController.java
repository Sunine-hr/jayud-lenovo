package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCommodityFollowForm;
import com.jayud.scm.model.po.Commodity;
import com.jayud.scm.model.po.CommodityFollow;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.service.ICommodityFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品操作日志表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/commodityFollow")
@Api(tags = "商品操作日志管理")
public class CommodityFollowController {

    @Autowired
    private ICommodityFollowService commodityFollowService;

    @ApiOperation(value = "根据商品id获取商品操作日志")
    @PostMapping(value = "/findListByCommodityId")
    public CommonResult findListByCommodityId(@RequestParam("id") Integer id) {
        List<CommodityFollowVO> commodityFollowVOS = commodityFollowService.findListByCommodityId(id);
        return CommonResult.success(commodityFollowVOS);
    }

    @ApiOperation(value = "新增操作信息")
    @PostMapping(value = "/AddCommodityFollow")
    public CommonResult AddCommodityFollow(@RequestBody AddCommodityFollowForm followForm) {
        boolean result = commodityFollowService.AddCommodityFollow(followForm);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"跟踪信息添加失败");
    }

}

