package com.jayud.scm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHubShippingDeliverForm;
import com.jayud.scm.model.bo.DispatchForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HubShippingDeliverMapper;
import com.jayud.scm.model.vo.HubShippingDeliverVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 调度配送表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-11-12
 */
@Service
public class HubShippingDeliverServiceImpl extends ServiceImpl<HubShippingDeliverMapper, HubShippingDeliver> implements IHubShippingDeliverService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private ITaxInvoiceService taxInvoiceService;

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private IHubShippingService hubShippingService;

    @Override
    public boolean saveOrUpdateHubShippingDeliver(AddHubShippingDeliverForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HubShippingDeliver hubShippingDeliver = ConvertUtil.convert(form, HubShippingDeliver.class);
        if(form.getId() != null){
            hubShippingDeliver.setMdyByName(systemUser.getUserName());
            hubShippingDeliver.setMdyBy(systemUser.getId().intValue());
            hubShippingDeliver.setMdyByDtm(LocalDateTime.now());
        }else{
            hubShippingDeliver.setDeliverNo(commodityService.getOrderNo(NoCodeEnum.HUB_SHIPPING_DELIVER.getCode(),LocalDateTime.now()));
            hubShippingDeliver.setCrtBy(systemUser.getId().intValue());
            hubShippingDeliver.setCrtByDtm(LocalDateTime.now());
            hubShippingDeliver.setCrtByName(systemUser.getUserName());
        }
        boolean result = this.saveOrUpdate(hubShippingDeliver);
        if(result){
            log.warn("新增或修改调度策略信息成功");
        }
        return result;
    }

    @Override
    public HubShippingDeliverVO getHubShippingDeliverById(Integer id) {
        HubShippingDeliver shippingDeliver = this.getById(id);
        HubShippingDeliverVO convert = ConvertUtil.convert(shippingDeliver, HubShippingDeliverVO.class);
        return convert;
    }

    @Override
    public boolean shippingDeliverTruckSend(Integer id) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HubShippingDeliver hubShippingDeliver = new HubShippingDeliver();
        hubShippingDeliver.setId(id);
        hubShippingDeliver.setStateFlag(2);
        hubShippingDeliver.setMdyBy(systemUser.getId().intValue());
        hubShippingDeliver.setMdyByDtm(LocalDateTime.now());
        hubShippingDeliver.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(hubShippingDeliver);
        if(result){
            log.warn("发车成功");
            boolean result1 = this.hubShippingService.updateHubShippingByDeliverId(id);
            if(result1){
                log.warn("出库单状态修改成功");
            }
        }
        return result;
    }

    @Override
    public boolean deliverStatusBack(Integer id) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HubShippingDeliver hubShippingDeliver = new HubShippingDeliver();
        hubShippingDeliver.setId(id);
        hubShippingDeliver.setStateFlag(1);
        hubShippingDeliver.setMdyBy(systemUser.getId().intValue());
        hubShippingDeliver.setMdyByDtm(LocalDateTime.now());
        hubShippingDeliver.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(hubShippingDeliver);
        if(result){
            log.warn("状态撤回成功");
            boolean result1 = this.hubShippingService.updateHubShippingStateByDeliverId(id);
            if(result1){
                log.warn("出库单状态回撤成功");
            }
        }
        return result;
    }

    @Override
    public boolean dispatch(DispatchForm form) {
        HubShippingDeliver hubShippingDeliver = this.getById(form.getId());

        if(CollectionUtil.isNotEmpty(form.getShipIds())){
            boolean result = hubShippingService.dispatch(form.getShipIds(),form.getId(),hubShippingDeliver.getDeliverNo());
            if(!result){
                log.warn("调度出口单失败");
                return false;
            }
        }
        if(CollectionUtil.isNotEmpty(form.getCheckIds())){
            boolean result = checkOrderService.dispatch(form.getCheckIds(),form.getId(),hubShippingDeliver.getDeliverNo());
            if(!result){
                log.warn("调度提验货单失败");
                return false;
            }
        }
        if(CollectionUtil.isNotEmpty(form.getTaxInvoiceIds())){
            boolean result = taxInvoiceService.dispatch(form.getTaxInvoiceIds(),form.getId(),hubShippingDeliver.getDeliverNo());
            if(!result){
                log.warn("调度发票失败");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean deleteDispatch(DispatchForm form) {
        HubShippingDeliver hubShippingDeliver = this.getById(form.getId());

        if(CollectionUtil.isNotEmpty(form.getShipIds())){
            boolean result = hubShippingService.deleteDispatch(form.getShipIds());
            if(!result){
                log.warn("删除出口单失败");
                return false;
            }
        }
        if(CollectionUtil.isNotEmpty(form.getCheckIds())){
            boolean result = checkOrderService.deleteDispatch(form.getCheckIds());
            if(!result){
                log.warn("删除提验货单失败");
                return false;
            }
        }
        if(CollectionUtil.isNotEmpty(form.getTaxInvoiceIds())){
            boolean result = taxInvoiceService.deleteDispatch(form.getTaxInvoiceIds());
            if(!result){
                log.warn("删除发票失败");
                return false;
            }
        }
        return true;
    }

    @Override
    public HubShippingDeliver saveHubShippingDeliver(HubShippingDeliver hubShippingDeliver) {

        hubShippingDeliver.setDeliverNo(commodityService.getOrderNo(NoCodeEnum.HUB_SHIPPING_DELIVER.getCode(),LocalDateTime.now()));
        hubShippingDeliver.setCrtByDtm(LocalDateTime.now());
        boolean save = this.save(hubShippingDeliver);
        if(save){
            return hubShippingDeliver;
        }
        return null;
    }
}
