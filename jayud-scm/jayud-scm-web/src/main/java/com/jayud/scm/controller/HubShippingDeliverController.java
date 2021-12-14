package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.model.vo.HubShippingDeliverVO;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 调度配送表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-11-12
 */
@RestController
@RequestMapping("/hubShippingDeliver")
@Api(tags = "调度配送")
public class HubShippingDeliverController {

    @Autowired
    private IHubShippingDeliverService hubShippingDeliverService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private IHubShippingService hubShippingService;

    @ApiOperation(value = "新增调度策略信息")
    @PostMapping(value = "/saveOrUpdateHubShippingDeliver")
    public CommonResult saveOrUpdateHubShippingDeliver(@RequestBody AddHubShippingDeliverForm form) {
        boolean result = hubShippingDeliverService.saveOrUpdateHubShippingDeliver(form);
        if(!result){
            return CommonResult.error(444,"新增或修改提货验货单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取调度策略信息")
    @PostMapping(value = "/getHubShippingDeliverById")
    public CommonResult<HubShippingDeliverVO> getHubShippingDeliverById(@RequestBody QueryForm form) {
        return CommonResult.success(this.hubShippingDeliverService.getHubShippingDeliverById(form.getId()));
    }

    @ApiOperation(value = "发车")
    @PostMapping(value = "/shippingDeliverTruckSend")
    public CommonResult shippingDeliverTruckSend(@RequestBody QueryForm form) {

        HubShippingDeliver hubShippingDeliver = this.hubShippingDeliverService.getById(form.getId());
        if(!hubShippingDeliver.getCheckStateFlag().equals("Y")){
            return CommonResult.error(444,"未审核的记录不能发车");
        }
        if(!hubShippingDeliver.getStateFlag().equals(1)){
            return CommonResult.error(444,"请选择未发车的记录");
        }

        boolean result = this.hubShippingDeliverService.shippingDeliverTruckSend(form.getId());
        if(!result){
            return CommonResult.error(444,"发车失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "状态回撤")
    @PostMapping(value = "/deliverStatusBack")
    public CommonResult deliverStatusBack(@RequestBody QueryForm form) {
        boolean result = this.hubShippingDeliverService.deliverStatusBack(form.getId());
        if(!result){
            return CommonResult.error(444,"状态回撤失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "调度")
    @PostMapping(value = "/dispatch")
    public CommonResult dispatch(@RequestBody DispatchForm form) {
        boolean result = this.hubShippingDeliverService.dispatch(form);
        if(!result){
            return CommonResult.error(444,"调度失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "删除绑定的出库单、提验货单、发票单")
    @PostMapping(value = "/deleteDispatch")
    public CommonResult deleteDispatch(@RequestBody DispatchForm form) {
        boolean result = this.hubShippingDeliverService.deleteDispatch(form);
        if(!result){
            return CommonResult.error(444,"删除失败");
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "策略调度审核")
    @PostMapping(value = "/auditHubShippingDeliver")
    public CommonResult auditHubShippingDeliver(@RequestBody PermissionForm form) {

        QueryWrapper<CheckOrder> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CheckOrder::getShippingDeliverId,form.getId());
        List<CheckOrder> checkOrders = checkOrderService.list(queryWrapper);

        QueryWrapper<HubShipping> queryWrapper1 = new QueryWrapper();
        queryWrapper1.lambda().eq(HubShipping::getDeliverId,form.getId());
        List<HubShipping> hubShippings = hubShippingService.list(queryWrapper1);

        if(CollectionUtil.isEmpty(checkOrders)&&CollectionUtil.isEmpty(hubShippings)){
            return CommonResult.error(444,"请为该记录添加调度详情后再进行审核！");
        }

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        if(!systemUser.getUserName().equals("admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
//            for (SystemRole systemRole : enabledRolesByUserId) {
//                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
//                if(systemRoleAction == null){
//                    return CommonResult.error(444,"该用户没有该按钮权限");
//                }
//            }
            List<Long> longs = new ArrayList<>();
            for (SystemRole systemRole : enabledRolesByUserId) {
                longs.add(systemRole.getId());
//                if(systemRoleAction == null){
//                    return CommonResult.error(444,"该用户没有该按钮权限");
//                }
            }
            List<SystemRoleAction> systemRoleActions = systemRoleActionService.getSystemRoleActionByRoleIdsAndActionCode(longs,form.getActionCode());
            if(CollectionUtil.isEmpty(systemRoleActions)){
                return CommonResult.error(444,"该用户没有该按钮权限");
            }
        }

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());

        return customerService.toExamine(form);

    }


}

