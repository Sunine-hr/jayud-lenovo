package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.CheckStateEnum;
import com.jayud.scm.model.enums.StateFlagEnum;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import javax.swing.plaf.nimbus.State;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提验货主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/checkOrder")
@Api(tags = "提验货管理")
@Slf4j
public class CheckOrderController {

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private ICheckOrderEntryService checkOrderEntryService;

    @Autowired
    private IHubReceivingService hubReceivingService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private IBookingOrderFollowService bookingOrderFollowService;

    @Autowired
    private IHgBillService hgBillService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;


    @ApiOperation(value = "下一步操作 1提货完成、2验货完成、3提货撤销、4验货撤销")
    @PostMapping(value = "/nextOperation")
    public CommonResult nextOperation(@RequestBody QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CheckOrder checkOrder = checkOrderService.getById(form.getId());
        BookingOrder bookingOrder = bookingOrderService.getById(checkOrder.getBookingId());
        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        String checkState = checkOrder.getCheckState();
//        if(checkState.equals("0")){
//            return CommonResult.error(444,"订单未审核，请前往审核");
//        }
        if(checkState.equals("2")){
            return CommonResult.error(444,"订单已取消，无法操作");
        }
        if(form.getNext().equals(2)){
            List<CheckOrderEntry> checkOrderEntries = this.checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrder.getId().longValue());
            BigDecimal nw = new BigDecimal(0);
            BigDecimal gw = new BigDecimal(0);
            for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {
                nw = nw.add(checkOrderEntry.getNw() != null ? checkOrderEntry.getNw() : new BigDecimal(0));
                gw = gw.add(checkOrderEntry.getGw() != null ? checkOrderEntry.getGw() : new BigDecimal(0));
                if(checkOrderEntry.getNw() == null){
                    return CommonResult.error(444,"净重为空不能进行验货完成操作");
                }
            }
            if(gw.compareTo(nw)<1){
                return CommonResult.error(444,"总毛重小于等于总净重不能进行验货完成操作");
            }
        }
        switch (form.getNext()){
            case 1:
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_3.getCode().toString());
                bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_2.getCode());
                bookingOrderFollow.setBookingId(bookingOrder.getId());
                bookingOrderFollow.setFollowContext("该订单已提货完成");
                bookingOrderFollow.setSType("人工");
                break;
            case 2:
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_5.getCode().toString());
                bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_2.getCode());
                bookingOrderFollow.setBookingId(bookingOrder.getId());
                bookingOrderFollow.setFollowContext("该订单已验货完成");
                bookingOrderFollow.setSType("人工");
                break;
            case 3:
                if(!checkState.equals("3")){
                    return CommonResult.error(444,"只有当前状态为提货完成的单，才能进行提货撤销");
                }
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_1.getCode().toString());
                break;
            case 4:
                if(!checkState.equals("5")){
                    return CommonResult.error(444,"只有当前状态为验货完成的单，才能进行验货撤销");
                }
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_3.getCode().toString());
                break;
        }
        boolean update = checkOrderService.updateById(checkOrder);
        if(!update){
            return CommonResult.error(444,"操作失败");
        }
        boolean result = bookingOrderService.updateById(bookingOrder);
        if(result){
            if(bookingOrderFollow.getBookingId() != null){
                boolean save = bookingOrderFollowService.save(bookingOrderFollow);
                if(save){
                    log.warn("委托单状态改变，操作记录添加成功");
                }
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "入库") //
    @PostMapping(value = "/warehousing")
    @Transactional
    public CommonResult warehousing(@RequestBody QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CheckOrder checkOrder = checkOrderService.getById(form.getId());
        checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_6.getCode().toString());
        boolean result = hubReceivingService.addHubReceiving(form);
        if(!result){
            return CommonResult.error(444,"入库失败");
        }
        boolean update = checkOrderService.updateById(checkOrder);
        if(!update){
            return CommonResult.error(444,"操作失败");
        }
        //判断是否入库完成，入库完成增加一个报关单
        boolean result1 = bookingOrderService.isCommplete(checkOrder.getBookingId());
        BookingOrder bookingOrder = bookingOrderService.getById(checkOrder.getBookingId());

        if(result1 && bookingOrder.getBillId() == null){
            //新增一个报关单
            Integer save = hgBillService.addHgBill(checkOrder.getBookingId());
            bookingOrder.setBillId(save);
            bookingOrder.setStateFlag(StateFlagEnum.STATE_FLAG_4.getCode());
            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setBookingId(bookingOrder.getId());
            bookingOrderFollow.setSType("人工");
            bookingOrderFollow.setFollowContext("订单入库成功");
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            boolean save1 = bookingOrderFollowService.save(bookingOrderFollow);
            if(save1){
                log.warn("委托单入库，操作记录添加成功");
            }
            boolean result2 = bookingOrderService.updateById(bookingOrder);
            if(result2){
                log.warn("修改委托单billId");
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据委托单id获取提验货信息")
    @PostMapping(value = "/getCheckOrderByBookingId")
    public CommonResult<List<CheckOrderVO>> getCheckOrderByBookingId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        List<Integer> list = new ArrayList<>();
        for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
            list.add(bookingOrder.getId());
        }
        form.setIds(list);
        List<CheckOrderVO> checkOrderVOList = checkOrderService.getCheckOrderByBookingId(form);
        return CommonResult.success(checkOrderVOList);
    }

    @ApiOperation(value = "自动生成提验货单")
    @PostMapping(value = "/automaticGenerationCheckOrder")
    public CommonResult automaticGenerationCheckOrder(@RequestBody QueryCommonForm form) {
        //判断委托单是否审核
        BookingOrder bookingOrder = bookingOrderService.getById(form.getId());
        if(!bookingOrder.getCheckStateFlag().equals("Y")){
            return CommonResult.error(444,"委托单未审核，不能进行操作");
        }
        return checkOrderService.automaticGenerationCheckOrder(form);

    }

    @ApiOperation(value = "新增提验货单")
    @PostMapping(value = "/saveOrUpdateCheckOrder")
    public CommonResult saveOrUpdateCheckOrder(@RequestBody AddCheckOrderForm form) {
//        List<AddCheckOrderEntryForm> checkOrderEntryForms = form.getCheckOrderEntryForms();
//        for (AddCheckOrderEntryForm checkOrderEntryForm : checkOrderEntryForms) {
//
//        }
        //根据委托单号判断是否已生成提验货
        if(CollectionUtils.isEmpty(form.getBookingOrderEntryList())){
            return CommonResult.error(444,"提验货详情不为空");
        }

        boolean result = checkOrderService.saveOrUpdateCheckOrder(form);
        if(!result){
            return CommonResult.error(444,"新增或修改提货验货单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取提验货单信息")
    @PostMapping(value = "/getCheckOrderById")
    public CommonResult<CheckOrderVO> getCheckOrderById(@RequestBody QueryForm form) {
        return CommonResult.success(this.checkOrderService.getCheckOrderById(form.getId()));
    }

    @ApiOperation(value = "提验货反审")
    @PostMapping(value = "/checkOrderAudit")
    public CommonResult checkOrderAudit(@RequestBody PermissionForm form) {

        CheckOrder checkOrder = checkOrderService.getById(form.getId());
        if(!checkOrder.getCheckState().equals("1")){
            return CommonResult.error(444,"状态只有为已提交，才能进行反审");
        }

        if(checkOrder.getShippingDeliverId() != null){
            return CommonResult.error(444,"提验货单已进行调度，不能反审");
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

}

