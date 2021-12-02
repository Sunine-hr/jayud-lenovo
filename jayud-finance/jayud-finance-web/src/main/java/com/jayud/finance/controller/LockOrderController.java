package com.jayud.finance.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.bo.AddLockOrderForm;
import com.jayud.finance.po.LockOrder;
import com.jayud.finance.service.ILockOrderService;
import com.jayud.finance.vo.LockOrderVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 锁单表 前端控制器
 * </p>
 *
 * @author chuanmei
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/lockOrder")
public class LockOrderController {

    @Autowired
    private ILockOrderService lockOrderService;

    /**
     * 设置锁单范围
     */
    @PostMapping(value = "/doLockOrder")
    @ApiOperation("设置核算期")
    public CommonResult doLockOrder(@RequestBody @Valid AddLockOrderForm form) {
        this.lockOrderService.saveOrUpdate(form);
        return CommonResult.success();
    }

    /**
     * 获取锁单期间
     */
    @PostMapping(value = "/getLockOrderById")
    @ApiOperation("查询锁核算期")
    public CommonResult getLockOrderById(@RequestBody Map<String, Object> map) {
//        Integer id = MapUtil.getInt(map, "id");
        Integer type = MapUtil.getInt(map, "type");
        Integer model = MapUtil.getInt(map, "model");
        if (type == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<LockOrder> lockOrders = this.lockOrderService.getByCondition(new LockOrder().setType(type).setModel(model));
        if (CollectionUtils.isEmpty(lockOrders)) {
            return CommonResult.success();
        }
        LockOrder lockOrder = lockOrders.get(0);
        LockOrderVO lockOrderVO = ConvertUtil.convert(lockOrder, LockOrderVO.class);
        List<String> times = new ArrayList<>();
        times.add(lockOrderVO.getStartTime());
        if (!StringUtils.isEmpty(lockOrderVO.getEndTime())) {
            times.add(lockOrderVO.getEndTime());
        }
        lockOrderVO.setTimes(times);
        return CommonResult.success(lockOrderVO);
    }
}

