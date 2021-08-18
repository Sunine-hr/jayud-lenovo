package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCustomerForm;
import com.jayud.scm.model.vo.HubReceivingVO;
import com.jayud.scm.service.IHubReceivingService;
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
 * 入库主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubReceiving")
@Api(tags = "入库管理")
public class HubReceivingController {

    @Autowired
    private IHubReceivingService hubReceivingService;

    @ApiOperation(value = "根据id查询入库订单信息")
    @PostMapping(value = "/getHubReceivingById")
    public CommonResult<HubReceivingVO> getHubReceivingById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");

        HubReceivingVO hubReceivingVO = hubReceivingService.getHubReceivingById(id);

        return CommonResult.success(hubReceivingVO);
    }

}

