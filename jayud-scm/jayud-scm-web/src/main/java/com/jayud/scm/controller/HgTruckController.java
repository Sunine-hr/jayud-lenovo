package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.CustomerVO;
import com.jayud.scm.model.vo.HgTruckVO;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 港车运输主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/hgTruck")
@Api(tags = "港车运输管理")
public class HgTruckController {

    @Autowired
    private IHgTruckService hgTruckService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;


    @ApiOperation(value = "根据id查询港车运输信息")
    @PostMapping(value = "/getHgTruckById")
    public CommonResult<HgTruckVO> getHgTruckById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        HgTruckVO hgTruckVO = hgTruckService.getHgTruckById(id);
        return CommonResult.success(hgTruckVO);
    }


    @ApiOperation(value = "添加或修改港车运输信息")
    @PostMapping(value = "/saveOrUpdateHgTruck")
    public CommonResult saveOrUpdateHgTruck(@RequestBody @Valid AddHgTruckForm form) {
        form.setTruckCompany(customerService.getCustomerById(form.getTruckCompanyId()).getCustomerName());
        boolean result = hgTruckService.saveOrUpdateHgTruck(form);
        if(!result){
            return CommonResult.error(444,"添加或修改港车运输失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "绑车")
    @PostMapping(value = "/tieUpCar")
    public CommonResult tieUpCar(@RequestBody @Valid QueryCommonForm form) {
        boolean result = hgTruckService.tieUpCar(form);
        if(!result){
            return CommonResult.error(444,"绑车操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改车次状态")
    @PostMapping(value = "/updateTrainNumberStatus")
    public CommonResult updateTrainNumberStatus(@RequestBody @Valid QueryCommonForm form) {
        boolean result = hgTruckService.updateTrainNumberStatus(form);
        if(!result){
            return CommonResult.error(444,"修改车次状态操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "封条信息录入")
    @PostMapping(value = "/sealInformationEntry")
    public CommonResult sealInformationEntry(@RequestBody @Valid QueryCommonForm form) {
        boolean result = hgTruckService.sealInformationEntry(form);
        if(!result){
            return CommonResult.error(444,"封条信息录入操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "绑车车牌")
    @PostMapping(value = "/tieLicensePlate")
    public CommonResult tieLicensePlate(@RequestBody @Valid HgTruckLicensePlateForm form) {
        form.setTruckCompany(customerService.getCustomerById(form.getTruckCompanyId()).getCustomerName());
        boolean result = hgTruckService.tieLicensePlate(form);
        if(!result){
            return CommonResult.error(444,"绑车车牌操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "解绑车次")
    @PostMapping(value = "/unboundTrainNumber")
    public CommonResult unboundTrainNumber(@RequestBody @Valid QueryCommonForm form) {
        boolean result = hgTruckService.unboundTrainNumber(form);
        if(!result){
            return CommonResult.error(444,"解绑车次操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "港车审核")
    @PostMapping(value = "/portCarAudit")
    public CommonResult portCarAudit(@RequestBody PermissionForm form) {

        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        if(CollectionUtils.isEmpty(bookingOrderByHgTrackId)){
            return CommonResult.error(444,"该港车单还未绑车，无法进行审核");
        }

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        if(!systemUser.getUserName().equals("admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
            for (SystemRole systemRole : enabledRolesByUserId) {
                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
                if(systemRoleAction == null){
                    return CommonResult.error(444,"该用户没有该按钮权限");
                }
            }
        }

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());

        return customerService.toExamine(form);

    }

}

