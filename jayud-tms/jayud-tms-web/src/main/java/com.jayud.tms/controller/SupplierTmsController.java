package com.jayud.tms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.UserTypeEnum;
import com.jayud.common.utils.BeanUtils;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.vo.OrderTransportInfoVO;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.service.IOrderTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/supplier")
@Api(tags = "web中港供应商")
public class SupplierTmsController {

    @Autowired
    private IOrderTransportService orderTransportService;

    @ApiOperation(value = "分页查询供应商订单")
    @PostMapping("/findSupplierTmsByPage")
    public CommonResult<CommonPageResult<OrderTransportVO>> findSupplierTmsByPage(@RequestBody QueryOrderTmsForm form) {
        form.setAccountType(UserTypeEnum.SUPPLIER_TYPE.getCode());
        IPage<OrderTransportVO> pageList = orderTransportService.findTransportOrderByPage(form);
        CommonPageResult<OrderTransportVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询供应商订单详情")
    @PostMapping("/getSupplierTmsById")
    public CommonResult<OrderTransportInfoVO> getSupplierTmsById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, BeanUtils.convertToFieldName(OrderTransportVO::getId));
        OrderTransportInfoVO details = this.orderTransportService.getDetailsById(id);
        details.doFilterData(UserTypeEnum.SUPPLIER_TYPE.getCode());
        return CommonResult.success(details);
    }


}
