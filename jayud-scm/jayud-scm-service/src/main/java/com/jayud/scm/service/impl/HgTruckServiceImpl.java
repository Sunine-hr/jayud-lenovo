package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHgTruckForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.HgTruckLicensePlateForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HgTruckMapper;
import com.jayud.scm.model.vo.HgTruckVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 港车运输主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class HgTruckServiceImpl extends ServiceImpl<HgTruckMapper, HgTruck> implements IHgTruckService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private IBookingOrderFollowService bookingOrderFollowService;

    @Autowired
    private IHgTruckFollowService hgTruckFollowService;

    @Override
    public HgTruckVO getHgTruckById(Integer id) {
        HgTruck hgTruck = this.getById(id);
        HgTruckVO hgTruckVO = ConvertUtil.convert(hgTruck, HgTruckVO.class);
        return hgTruckVO;
    }

    @Override
    public boolean saveOrUpdateHgTruck(AddHgTruckForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgTruckFollow hgTruckFollow = new HgTruckFollow();
        HgTruck hgTruck = ConvertUtil.convert(form, HgTruck.class);
        if(hgTruck.getId() != null){
            hgTruck.setMdyBy(systemUser.getId().intValue());
            hgTruck.setMdyByDtm(LocalDateTime.now());
            hgTruck.setMdyByName(systemUser.getUserName());
            hgTruckFollow.setSType(OperationEnum.UPDATE.getCode());
            hgTruckFollow.setFollowContext(OperationEnum.UPDATE.getCode()+hgTruck.getId());
        }else{
            hgTruck.setTruckNo(commodityService.getOrderNo(NoCodeEnum.HG_TRUCK.getCode(),LocalDateTime.now()));
            hgTruck.setCrtBy(systemUser.getId().intValue());
            hgTruck.setCrtByDtm(LocalDateTime.now());
            hgTruck.setCrtByName(systemUser.getUserName());
            hgTruckFollow.setSType(OperationEnum.INSERT.getCode());
            hgTruckFollow.setFollowContext(OperationEnum.INSERT.getCode()+hgTruck.getTruckNo());
        }
        boolean update = this.saveOrUpdate(hgTruck);
        if(update){
           log.warn("添加或修改港车运输信息成功"+hgTruck);
            hgTruckFollow.setMdyBy(systemUser.getId().intValue());
            hgTruckFollow.setMdyByDtm(LocalDateTime.now());
            hgTruckFollow.setMdyByName(systemUser.getUserName());
            boolean save = hgTruckFollowService.save(hgTruckFollow);
            if(save){
                log.warn("添加或修改港车运输信息,操作日志添加成功");
            }
        }
        return update;
    }

    @Override
    public boolean tieUpCar(QueryCommonForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<BookingOrderFollow> bookingOrderFollowList = new ArrayList<>();
        List<BookingOrder> bookingOrders = new ArrayList<>();
        //通过港车id获取港车信息
        HgTruck hgTruck = this.getById(form.getId());
        //获取委托单id集合
        for (Integer id : form.getIds()) {
            BookingOrder bookingOrder = new BookingOrder();
            bookingOrder.setId(id);
            bookingOrder.setHgTruckId(hgTruck.getId());
            bookingOrder.setHgTruckNo(hgTruck.getTruckNo());
            bookingOrder.setMdyBy(systemUser.getId().intValue());
            bookingOrder.setMdyByDtm(LocalDateTime.now());
            bookingOrder.setMdyByName(systemUser.getUserName());
            bookingOrders.add(bookingOrder);

            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setBookingId(id);
            bookingOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            bookingOrderFollow.setFollowContext("绑定车辆"+hgTruck.getTruckNo());
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            bookingOrderFollowList.add(bookingOrderFollow);
        }
        boolean b = this.bookingOrderService.updateBatchById(bookingOrders);
        if(b){
            log.warn("绑车成功");
            boolean b1 = this.bookingOrderFollowService.saveBatch(bookingOrderFollowList);
            log.warn("修改委托单成功");
            HgTruckFollow hgTruckFollow = new HgTruckFollow();
            hgTruckFollow.setSType(OperationEnum.UPDATE.getCode());
            hgTruckFollow.setFollowContext(systemUser.getUserName()+"绑车"+form.getIds());
            hgTruckFollow.setCrtBy(systemUser.getId().intValue());
            hgTruckFollow.setCrtByDtm(LocalDateTime.now());
            hgTruckFollow.setCrtByName(systemUser.getUserName());
            boolean save = hgTruckFollowService.save(hgTruckFollow);
            if(save){
                log.warn("绑车操作成功,操作日志添加成功");
            }
        }
        return b;
    }

    @Override
    public boolean updateTrainNumberStatus(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgTruck hgTruck = new HgTruck();
        hgTruck.setId(form.getId());
        hgTruck.setStateFlag(form.getStatus());
        hgTruck.setMdyBy(systemUser.getId().intValue());
        hgTruck.setMdyByDtm(LocalDateTime.now());
        hgTruck.setMdyByName(systemUser.getUserName());
        boolean update = this.updateById(hgTruck);
        if(update){
            HgTruckFollow hgTruckFollow = new HgTruckFollow();
            hgTruckFollow.setSType(OperationEnum.UPDATE.getCode());
            hgTruckFollow.setFollowContext(systemUser.getUserName()+"修改车次状态为"+form.getStatus());
            hgTruckFollow.setCrtBy(systemUser.getId().intValue());
            hgTruckFollow.setCrtByDtm(LocalDateTime.now());
            hgTruckFollow.setCrtByName(systemUser.getUserName());
            boolean save = hgTruckFollowService.save(hgTruckFollow);
            if(save){
                log.warn("修改车次操作成功,操作日志添加成功");
            }
        }
        return update;
    }

    @Override
    public boolean sealInformationEntry(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgTruck hgTruck = new HgTruck();
        hgTruck.setId(form.getId());
        hgTruck.setLockColour(form.getLockColour());
        hgTruck.setLockNum(form.getLockNum());
        hgTruck.setMdyBy(systemUser.getId().intValue());
        hgTruck.setMdyByDtm(LocalDateTime.now());
        hgTruck.setMdyByName(systemUser.getUserName());
        boolean update = this.updateById(hgTruck);
        if(update){
            HgTruckFollow hgTruckFollow = new HgTruckFollow();
            hgTruckFollow.setSType(OperationEnum.UPDATE.getCode());
            hgTruckFollow.setFollowContext(systemUser.getUserName()+"录入封条信息");
            hgTruckFollow.setCrtBy(systemUser.getId().intValue());
            hgTruckFollow.setCrtByDtm(LocalDateTime.now());
            hgTruckFollow.setCrtByName(systemUser.getUserName());
            boolean save = hgTruckFollowService.save(hgTruckFollow);
            if(save){
                log.warn("封条信息录入操作成功,操作日志添加成功");
            }
        }
        return update;
    }

    @Override
    public boolean tieLicensePlate(HgTruckLicensePlateForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgTruck hgTruck = ConvertUtil.convert(form, HgTruck.class);
        hgTruck.setMdyBy(systemUser.getId().intValue());
        hgTruck.setMdyByDtm(LocalDateTime.now());
        hgTruck.setMdyByName(systemUser.getUserName());
        boolean update = this.updateById(hgTruck);
        if(update){
            HgTruckFollow hgTruckFollow = new HgTruckFollow();
            hgTruckFollow.setHgTruckId(hgTruck.getId());
            hgTruckFollow.setSType(OperationEnum.UPDATE.getCode());
            hgTruckFollow.setFollowContext(systemUser.getUserName()+"操作绑车车牌");
            hgTruckFollow.setCrtBy(systemUser.getId().intValue());
            hgTruckFollow.setCrtByDtm(LocalDateTime.now());
            hgTruckFollow.setCrtByName(systemUser.getUserName());
            boolean save = hgTruckFollowService.save(hgTruckFollow);
            if(save){
                log.warn("绑车车牌操作成功,操作日志添加成功");
            }
        }
        return update;
    }

    @Override
    public boolean unboundTrainNumber(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        BookingOrder bookingOrder = new BookingOrder();
        bookingOrder.setId(form.getId());
        bookingOrder.setHgTruckId(null);
        bookingOrder.setHgTruckNo(null);
        bookingOrder.setMdyBy(systemUser.getId().intValue());
        bookingOrder.setMdyByDtm(LocalDateTime.now());
        bookingOrder.setMdyByName(systemUser.getUserName());
        boolean update = this.bookingOrderService.updateById(bookingOrder);

        if(update){
            log.warn("解绑车次成功");
            BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
            bookingOrderFollow.setBookingId(form.getId());
            bookingOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            bookingOrderFollow.setFollowContext("解绑车次");
            bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
            bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
            bookingOrderFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.bookingOrderFollowService.save(bookingOrderFollow);
            if(!save){
                log.warn("解绑车次成功，委托单日志添加成功");
            }
        }

        return update;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {

        List<HgTruckFollow> hgTruckFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            HgTruckFollow hgTruckFollow = new HgTruckFollow();
            hgTruckFollow.setHgTruckId(id.intValue());
            hgTruckFollow.setSType(OperationEnum.DELETE.getCode());
            hgTruckFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            hgTruckFollow.setCrtBy(deleteForm.getId().intValue());
            hgTruckFollow.setCrtByDtm(deleteForm.getDeleteTime());
            hgTruckFollow.setCrtByName(deleteForm.getName());
            hgTruckFollows.add(hgTruckFollow);
        }

        boolean b1 = hgTruckFollowService.saveBatch(hgTruckFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+hgTruckFollows);
        }
        return b1;
    }

    @Override
    public boolean getManifest(String exHkNo, String truckNo) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        QueryWrapper<HgTruck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgTruck::getTruckNo,truckNo);
        queryWrapper.lambda().eq(HgTruck::getVoided,0);
        HgTruck hgTruck = this.getOne(queryWrapper);
        hgTruck.setExHkNo(exHkNo);
        boolean result = this.updateById(hgTruck);
        if(result){
            HgTruckFollow hgTruckFollow = new HgTruckFollow();
            hgTruckFollow.setHgTruckId(hgTruck.getId());
            hgTruckFollow.setSType(OperationEnum.UPDATE.getCode());
            hgTruckFollow.setFollowContext("数据接收成功，"+systemUser.getUserName()+"修改载货清单");
            hgTruckFollow.setCrtBy(systemUser.getId().intValue());
            hgTruckFollow.setCrtByDtm(LocalDateTime.now());
            hgTruckFollow.setCrtByName(systemUser.getUserName());
            boolean save = hgTruckFollowService.save(hgTruckFollow);
            if(save){
                log.warn("修改载货清单数据操作日志，添加成功");
            }
        }
        return result;
    }
}
