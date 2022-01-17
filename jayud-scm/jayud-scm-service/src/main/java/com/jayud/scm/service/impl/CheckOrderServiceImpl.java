package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.CheckOrderMapper;
import com.jayud.scm.model.vo.CheckOrderEntryVO;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提验货主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class CheckOrderServiceImpl extends ServiceImpl<CheckOrderMapper, CheckOrder> implements ICheckOrderService {

    @Autowired
    private ICheckOrderEntryService checkOrderEntryService;

    @Autowired
    private ICheckOrderFollowService checkOrderFollowService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IBookingOrderFollowService bookingOrderFollowService;

    @Autowired
    private IBookingOrderEntryService bookingOrderEntryService;

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<CheckOrderEntry> checkOrderEntries = new ArrayList<>();
        List<CheckOrderFollow> checkOrderFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            List<CheckOrderEntry> checkOrderEntries1 = checkOrderEntryService.getCheckOrderEntryByCheckOrderId(id);
            for (CheckOrderEntry checkOrderEntry : checkOrderEntries1) {
                checkOrderEntry.setVoidedBy(deleteForm.getId().intValue());
                checkOrderEntry.setVoidedByDtm(deleteForm.getDeleteTime());
                checkOrderEntry.setVoidedByName(deleteForm.getName());
                checkOrderEntry.setVoided(1);
                checkOrderEntries.add(checkOrderEntry);
            }

            CheckOrderFollow checkOrderFollow = new CheckOrderFollow();
            checkOrderFollow.setCheckId(id.intValue());
            checkOrderFollow.setSType(OperationEnum.DELETE.getCode());
            checkOrderFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            checkOrderFollow.setCrtBy(deleteForm.getId().intValue());
            checkOrderFollow.setCrtByDtm(deleteForm.getDeleteTime());
            checkOrderFollow.setCrtByName(deleteForm.getName());
            checkOrderFollows.add(checkOrderFollow);
        }
        //删除出库明细
        boolean update = checkOrderEntryService.updateBatchById(checkOrderEntries);
        if(!update){
            log.warn("删除出库订单详情失败");
        }

        boolean b1 = checkOrderFollowService.saveBatch(checkOrderFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+checkOrderFollows);
        }
        return b1;
    }

    @Override
    public List<CheckOrderVO> getCheckOrderByBookingId(QueryCommonForm form) {
        List<CheckOrderVO> checkOrderVOS = new ArrayList<>();
        for (Integer id : form.getIds()) {
            QueryWrapper<CheckOrder> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(CheckOrder::getBookingId,id);
            List<CheckOrder> list = this.list(queryWrapper);
            List<CheckOrderVO> checkOrderVOS1 = ConvertUtil.convertList(list, CheckOrderVO.class);
            for (CheckOrderVO checkOrderVO : checkOrderVOS1) {
                List<CheckOrderEntry> checkOrderEntryByCheckOrderId = checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrderVO.getId().longValue());
                checkOrderVO.setBookingOrderEntryList(ConvertUtil.convertList(checkOrderEntryByCheckOrderId, CheckOrderEntryVO.class));
            }
            checkOrderVOS.addAll(checkOrderVOS1);
        }
        return checkOrderVOS;
    }

    @Override
    public CommonResult automaticGenerationCheckOrder(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Map<String,Object> map = new HashMap<>();
        map.put("orderId",form.getId());
        map.put("userId",systemUser.getId());
        map.put("userName",systemUser.getUserName());
        this.baseMapper.automaticGenerationCheckOrder(map);
        if(map.get("state").equals(1)){
            return CommonResult.error((Integer)map.get("state"),(String)map.get("string"));
        }
        BookingOrderFollow bookingOrderFollow = new BookingOrderFollow();
        bookingOrderFollow.setCrtBy(systemUser.getId().intValue());
        bookingOrderFollow.setCrtByDtm(LocalDateTime.now());
        bookingOrderFollow.setCrtByName(systemUser.getUserName());
        bookingOrderFollow.setBookingId(form.getId());
        bookingOrderFollow.setSType(OperationEnum.INSERT.getCode());
        bookingOrderFollow.setFollowContext("自动生成提验货成功");
        boolean save = bookingOrderFollowService.save(bookingOrderFollow);
        if(save){
            log.warn("自动生成提验货，操作日志添加成功");
        }
        return CommonResult.success();
    }

    @Override
    public boolean saveOrUpdateCheckOrder(AddCheckOrderForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        CheckOrderFollow checkOrderFollow = new CheckOrderFollow();
        CheckOrder checkOrder = ConvertUtil.convert(form, CheckOrder.class);

        BigDecimal totalQty = new BigDecimal(0);
        BigDecimal totaCbm = new BigDecimal(0);
        BigDecimal totalGw = new BigDecimal(0);
        BigDecimal totaNw = new BigDecimal(0);
        BigDecimal totaPackages = new BigDecimal(0) ;
        BigDecimal totaPallets = new BigDecimal(0);
        List<AddCheckOrderEntryForm> addCheckOrderEntryForms = form.getBookingOrderEntryList();
        for (AddCheckOrderEntryForm addCheckOrderEntryForm : addCheckOrderEntryForms) {
            totalQty = totalQty.add(addCheckOrderEntryForm.getQty()!=null ?addCheckOrderEntryForm.getQty():new BigDecimal(0));
            totaCbm = totaCbm.add(addCheckOrderEntryForm.getCbm()!=null ?addCheckOrderEntryForm.getCbm():new BigDecimal(0));
            totalGw = totalGw.add(addCheckOrderEntryForm.getGw()!=null ?addCheckOrderEntryForm.getGw():new BigDecimal(0));
            totaNw = totaNw.add(addCheckOrderEntryForm.getNw()!=null ?addCheckOrderEntryForm.getNw():new BigDecimal(0));
            totaPackages = totaPackages.add(addCheckOrderEntryForm.getPackages()!=null ?addCheckOrderEntryForm.getPackages(): new BigDecimal(0));
            totaPallets = totaPallets.add (addCheckOrderEntryForm.getPallets()!=null ?addCheckOrderEntryForm.getPallets():new BigDecimal(0)) ;
        }
        checkOrder.setTotalCbm(totaCbm);
        checkOrder.setTotalGw(totalGw);
        checkOrder.setTotalPackages(totaPackages);
        checkOrder.setTotalPallet(totaPallets.intValue());


        if(form.getId() != null){
            checkOrder.setMdyByName(systemUser.getUserName());
            checkOrder.setMdyBy(systemUser.getId().intValue());
            checkOrder.setMdyByDtm(LocalDateTime.now());
            checkOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            checkOrderFollow.setFollowContext(systemUser.getUserName()+"修改提验货单，单号为"+checkOrder.getCheckNo());
        }else{
            checkOrder.setCheckNo(commodityService.getOrderNo(NoCodeEnum.CHECK_ORDER.getCode(),LocalDateTime.now()));
            checkOrder.setCrtBy(systemUser.getId().intValue());
            checkOrder.setCrtByDtm(LocalDateTime.now());
            checkOrder.setCrtByName(systemUser.getUserName());
            checkOrderFollow.setSType(OperationEnum.INSERT.getCode());
            checkOrderFollow.setFollowContext(systemUser.getUserName()+"增加提验货单，单号为"+checkOrder.getCheckNo());
        }
        boolean result = this.saveOrUpdate(checkOrder);
        if(result){
            log.warn("增加或修改提验货单成功");
            if(CollectionUtils.isNotEmpty(form.getBookingOrderEntryList())){
                List<BookingOrderEntry> bookingOrderEntries = new ArrayList<>();
                List<CheckOrderEntry> checkOrderEntries = ConvertUtil.convertList(form.getBookingOrderEntryList(), CheckOrderEntry.class);
                for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {

                    if(checkOrderEntry.getId() != null){
                        checkOrderEntry.setCheckId(checkOrder.getId());
                        checkOrderEntry.setMdyBy(systemUser.getId().intValue());
                        checkOrderEntry.setMdyByDtm(LocalDateTime.now());
                        checkOrderEntry.setMdyByName(systemUser.getUserName());
                    }else{
                        checkOrderEntry.setCheckId(checkOrder.getId());
                        checkOrderEntry.setCrtBy(systemUser.getId().intValue());
                        checkOrderEntry.setCrtByDtm(LocalDateTime.now());
                        checkOrderEntry.setCrtByName(systemUser.getUserName());
                    }
                }
                boolean result1 = this.checkOrderEntryService.saveOrUpdateBatch(checkOrderEntries);
                if(!result1){
                    log.warn("修改或新增提验货单明细失败"+checkOrderEntries);
                }
                checkOrderFollow.setCrtBy(systemUser.getId().intValue());
                checkOrderFollow.setCrtByDtm(LocalDateTime.now());
                checkOrderFollow.setCrtByName(systemUser.getUserName());
                checkOrderFollow.setCheckId(checkOrder.getId());
                boolean save = this.checkOrderFollowService.save(checkOrderFollow);
                if(save){
                    log.warn("修改或者新增提验货单，提验货操作日志添加成功"+checkOrderFollow);
                }
//                for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {
//                    QueryWrapper<CheckOrderEntry> queryWrapper1 = new QueryWrapper<>();
//                    queryWrapper1.lambda().eq(CheckOrderEntry::getBookingEntryId,checkOrderEntry.getBookingEntryId());
//                    queryWrapper1.lambda().eq(CheckOrderEntry::getItemModel,checkOrderEntry.getItemModel());
//                    queryWrapper1.lambda().eq(CheckOrderEntry::getVoided,0);
//                    List<CheckOrderEntry> list = this.checkOrderEntryService.list(queryWrapper1);
//                    BigDecimal pallets = new BigDecimal(0);
//                    BigDecimal packages = new BigDecimal(0);
//                    BigDecimal gw = new BigDecimal(0);
//                    BigDecimal nw = new BigDecimal(0);
//                    for (CheckOrderEntry orderEntry : list) {
//                        pallets = pallets.add(orderEntry.getPallets() != null ? orderEntry.getPallets() : new BigDecimal(0));
//                        packages = packages.add(orderEntry.getPackages() != null ? orderEntry.getPackages() : new BigDecimal(0));
//                        gw = gw.add(orderEntry.getGw() != null ? orderEntry.getGw() : new BigDecimal(0));
//                        nw = nw.add(orderEntry.getNw() != null ? orderEntry.getNw() : new BigDecimal(0));
//                    }
//
//                    BookingOrderEntry bookingOrderEntry = new BookingOrderEntry();
//                    bookingOrderEntry.setId(checkOrderEntry.getBookingEntryId());
//                    bookingOrderEntry.setPallets(pallets);
//                    bookingOrderEntry.setItemOrigin(checkOrderEntry.getItemOrigin());
//                    bookingOrderEntry.setPackages(packages);
//                    bookingOrderEntry.setGw(gw);
//                    bookingOrderEntry.setNw(nw);
//                    bookingOrderEntry.setPackingType(checkOrderEntry.getPackagesType());
//                    bookingOrderEntry.setBn(checkOrderEntry.getBn());
//                    bookingOrderEntry.setCtnsNo(checkOrderEntry.getCtnsNo());
//                    bookingOrderEntry.setAccessories(checkOrderEntry.getItemAccs());
//                    bookingOrderEntries.add(bookingOrderEntry);
//                }
//
//                boolean b1 = this.bookingOrderEntryService.updateBatchById(bookingOrderEntries);
//                if(!b1){
//                    log.warn("回改订单明细失败"+bookingOrderEntries);
//                }
            }
        }
        return result;
    }

    @Override
    public CheckOrderVO getCheckOrderById(Integer id) {
        CheckOrder checkOrder = this.getById(id);
        CheckOrderVO checkOrderVO = ConvertUtil.convert(checkOrder, CheckOrderVO.class);
        List<CheckOrderEntry> checkOrderEntryByCheckOrderId = checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrder.getId().longValue());
        checkOrderVO.setBookingOrderEntryList(ConvertUtil.convertList(checkOrderEntryByCheckOrderId,CheckOrderEntryVO.class));
        return checkOrderVO;
    }

    @Override
    public boolean dispatch(List<Integer> checkIds, Integer id, String deliverNo) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<CheckOrderFollow> checkOrderFollows = new ArrayList<>();
        List<CheckOrder> checkOrders = new ArrayList<>();
        for (Integer checkId : checkIds) {
            CheckOrder checkOrder = new CheckOrder();
            checkOrder.setId(checkId);
            checkOrder.setShippingDeliverId(id);
            checkOrder.setMdyBy(systemUser.getId().intValue());
            checkOrder.setMdyByDtm(LocalDateTime.now());
            checkOrder.setMdyByName(systemUser.getUserName());
            checkOrders.add(checkOrder);

            CheckOrderFollow checkOrderFollow = new CheckOrderFollow();
            checkOrderFollow.setCheckId(checkId);
            checkOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            checkOrderFollow.setFollowContext("提验货单绑定配送车辆" + deliverNo);
            checkOrderFollow.setCrtBy(systemUser.getId().intValue());
            checkOrderFollow.setCrtByDtm(LocalDateTime.now());
            checkOrderFollow.setCrtByName(systemUser.getUserName());
            checkOrderFollows.add(checkOrderFollow);
        }
        boolean result = this.updateBatchById(checkOrders);
        if(result){
            log.warn("提验货单绑定配送车辆成功");
            boolean result1 = this.checkOrderFollowService.saveBatch(checkOrderFollows);
            if(result1){
                log.warn("提验货单绑定配送车辆操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean deleteDispatch(List<Integer> checkIds) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<CheckOrderFollow> checkOrderFollows = new ArrayList<>();
        List<CheckOrder> checkOrders = new ArrayList<>();
        for (Integer checkId : checkIds) {
            CheckOrder checkOrder = new CheckOrder();
            checkOrder.setId(checkId);
            checkOrder.setShippingDeliverId(null);
            checkOrder.setMdyBy(systemUser.getId().intValue());
            checkOrder.setMdyByDtm(LocalDateTime.now());
            checkOrder.setMdyByName(systemUser.getUserName());
            checkOrders.add(checkOrder);

            CheckOrderFollow checkOrderFollow = new CheckOrderFollow();
            checkOrderFollow.setCheckId(checkId);
            checkOrderFollow.setSType(OperationEnum.UPDATE.getCode());
            checkOrderFollow.setFollowContext("提验货单去绑配送车辆");
            checkOrderFollow.setCrtBy(systemUser.getId().intValue());
            checkOrderFollow.setCrtByDtm(LocalDateTime.now());
            checkOrderFollow.setCrtByName(systemUser.getUserName());
            checkOrderFollows.add(checkOrderFollow);
        }
        boolean result = this.updateBatchById(checkOrders);
        if(result){
            log.warn("提验货单去绑配送车辆成功");
            boolean result1 = this.checkOrderFollowService.saveBatch(checkOrderFollows);
            if(result1){
                log.warn("提验货单去绑配送车辆操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean saveErrorInformation(CheckOrder checkOrder) {

        checkOrder.setMdyByDtm(LocalDateTime.now());

        CheckOrderFollow checkOrderFollow = new CheckOrderFollow();
        checkOrderFollow.setCheckId(checkOrder.getId());
        checkOrderFollow.setSType(OperationEnum.UPDATE.getCode());
        checkOrderFollow.setFollowContext("提验货单去绑配送车辆");
        checkOrderFollow.setCrtByDtm(LocalDateTime.now());
        boolean result = this.updateById(checkOrder);
        if(result){
            log.warn("提验货单去绑配送车辆成功");
            boolean result1 = this.checkOrderFollowService.save(checkOrderFollow);
            if(result1){
                log.warn("提验货单去绑配送车辆操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    public List<CheckOrder> getCheckOrderByDeliverId(Integer id) {
        QueryWrapper<CheckOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CheckOrder::getShippingDeliverId,id);
        queryWrapper.lambda().eq(CheckOrder::getVoided,0);
        return this.list(queryWrapper);
    }
}
