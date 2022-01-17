package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCustomerForm;
import com.jayud.scm.model.po.HubReceivingEntry;
import com.jayud.scm.model.vo.HubReceivingVO;
import com.jayud.scm.service.IHubReceivingEntryService;
import com.jayud.scm.service.IHubReceivingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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

    @Autowired
    private IHubReceivingEntryService hubReceivingEntryService;

    @ApiOperation(value = "根据id查询入库订单信息")
    @PostMapping(value = "/getHubReceivingById")
    public CommonResult<HubReceivingVO> getHubReceivingById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");

        HubReceivingVO hubReceivingVO = hubReceivingService.getHubReceivingById(id);

        return CommonResult.success(hubReceivingVO);
    }

    @ApiOperation(value = "删除入库单")
    @PostMapping(value = "/deleteHubReceiving")
    public CommonResult deleteHubReceiving(@Valid @RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");

        List<HubReceivingEntry> receivingEntryByReceivingId = hubReceivingEntryService.getReceivingEntryByReceivingId(id.longValue());
        if(CollectionUtil.isNotEmpty(receivingEntryByReceivingId)){
            for (HubReceivingEntry hubReceivingEntry : receivingEntryByReceivingId) {
                if(hubReceivingEntry.getBillId() != null){
                    return CommonResult.error(444,"该记录已经生成报关单，无法删除，请先删除对应的报关单");
                }
            }

        }

        boolean result = hubReceivingService.deleteHubReceiving(id);
        if(!result){
            return CommonResult.error(444,"删除入库单失败");
        }
        return CommonResult.success();
    }

}

