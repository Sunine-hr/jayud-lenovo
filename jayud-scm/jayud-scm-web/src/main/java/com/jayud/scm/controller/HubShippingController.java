package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.AddHubShippingForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.model.vo.HubReceivingVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.*;
import com.jayud.scm.service.impl.CommodityServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubShipping")
@Api(tags = "出库管理")
public class HubShippingController {

    @Autowired
    private IHubShippingService hubShippingService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @ApiOperation(value = "根据id查询出库订单信息")
    @PostMapping(value = "/getHubShippingById")
    public CommonResult<HubShippingVO> getHubShippingById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");

        HubShippingVO hubShippingVO = hubShippingService.getHubShippingById(id);
        hubShippingVO.setModelTypeName(hubShippingVO.getModelType().toString());

        return CommonResult.success(hubShippingVO);
    }

    @ApiOperation(value = "新增或修改出库订单")
    @PostMapping(value = "/saveOrUpdateHubShipping")
    public CommonResult saveOrUpdateHubShipping(@RequestBody AddHubShippingForm form) {
        form.setModelType(Integer.parseInt(form.getModelTypeName()));
        boolean result = hubShippingService.saveOrUpdateHubShipping(form);
        if(!result){
            return CommonResult.error(444,"新增或修改出库订单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "签收出库单")
    @PostMapping(value = "/signOrder")
    public CommonResult signOrder(@RequestBody QueryCommonForm form) {

        HubShipping hubShipping = hubShippingService.getById(form.getIds().get(0));
        if(hubShipping.getSignState().equals(1)){
            return CommonResult.error(444,"该出库单已签收，无法重复签收");
        }

        if(hubShipping.getStateFlag().equals(1)){
            return CommonResult.error(444,"未出库的出库单不允许签收！");
        }

        boolean result = hubShippingService.signOrder(form);
        if(!result){
            return CommonResult.error(444,"出库单签收失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据委托单id获取出库单信息")
    @PostMapping(value = "/getHubShippingByBookingId")
    public CommonResult getHubShippingByBookingId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        List<Integer> list = new ArrayList<>();
        for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
            list.add(bookingOrder.getId());
        }
        form.setIds(list);
        List<HubShippingVO> hubShippingVOS = hubShippingService.getHubShippingByBookingId(form);
        return CommonResult.success(hubShippingVOS);
    }

    @ApiOperation(value = "自动出库")
    @PostMapping(value = "/automaticGenerationHubShipping")
    public CommonResult automaticGenerationHubShipping(@RequestBody QueryCommonForm form) {
        return hubShippingService.automaticGenerationHubShipping(form);
    }


    @ApiOperation(value = "出库单信息修改")
    @PostMapping(value = "/updateHubShipping")
    public CommonResult updateHubShipping(@RequestBody AddHubShippingForm form) {
        boolean result = hubShippingService.updateHubShipping(form);
        if(!result){
            return CommonResult.error(444,"出库单信息修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "签收出库单信息")
    @PostMapping(value = "/signHubShipping")
    public CommonResult signHubShipping(@RequestBody QueryCommonForm form) {
        if(null == form.getId()){
            return CommonResult.error(444,"出库单id不为空");
        }
        HubShipping hubShipping = hubShippingService.getById(form.getId());
        if(hubShipping.getStateFlag().equals(1)){
            return CommonResult.error(444,"未出库的出库单不允许签收！");
        }
        boolean result = hubShippingService.signHubShipping(form);
        if(!result){
            return CommonResult.error(444,"签收出库单信息失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "出库单反审")
    @PostMapping(value = "/hubShippingAudit")
    public CommonResult checkOrderAudit(@RequestBody PermissionForm form) {

        HubShipping hubShipping = hubShippingService.getById(form.getId());
        if(!hubShipping.getStateFlag().equals(0)){
            return CommonResult.error(444,"状态只有为未出库的单，才能进行反审");
        }

        if(hubShipping.getDeliverId() != null){
            return CommonResult.error(444,"出库单已进行调度，不能反审");
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

        return customerService.deApproval(form);

    }

    @ApiOperation(value = "出库审核")
    @PostMapping(value = "/reviewHubShipping")
    public CommonResult reviewHubShipping(@RequestBody PermissionForm form) {
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HubShipping hubShipping = this.hubShippingService.getById(form.getId());
        if(hubShipping.getShippingDate() == null){
            return CommonResult.error(444,"出库日期为空，无法审核");
        }
        if(hubShipping.getWhAddress() == null){
            return CommonResult.error(444,"交货地址为空，无法审核");
        }
        if(hubShipping.getDeliveryDate() == null){
            return CommonResult.error(444,"要求交货时间为空，无法审核");
        }

        //获取按钮权限
//        SystemAction systemAction = systemActionService.getSystemActionByActionCode(form.getActionCode());

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

        //拥有按钮权限，判断是否为审核按钮

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());

        return hubShippingService.reviewHubShipping(form);
    }


}

