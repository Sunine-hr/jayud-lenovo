package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HgBillMapper;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.hdgf.HDGFDiagram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报关单主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class HgBillServiceImpl extends ServiceImpl<HgBillMapper, HgBill> implements IHgBillService {

    @Autowired
    private IHgBillFollowService hgBillFollowService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IHubReceivingEntryService hubReceivingEntryService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private ICommodityService commodityService;

    @Override
    public List<HgBillVO> getHgBillByBookingId(QueryCommonForm form) {
        List<HgBillVO> hgBillVOS = new ArrayList<>();
        for (Integer id : form.getIds()) {
            hgBillVOS.add(ConvertUtil.convert(this.getById(id),HgBillVO.class));
        }
        return hgBillVOS;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<HubReceivingEntry> hubReceivingEntries = new ArrayList<>();
        List<HgBillFollow> hgBillFollows = new ArrayList<>();
        List<BookingOrder> bookingOrders = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            //去掉委托单中的报关id
            BookingOrder bookingOrderByBillId = bookingOrderService.getBookingOrderByBillId(id.intValue());
            bookingOrderByBillId.setBillId(null);
            bookingOrderByBillId.setMdyBy(deleteForm.getId().intValue());
            bookingOrderByBillId.setMdyByDtm(deleteForm.getDeleteTime());
            bookingOrderByBillId.setMdyByName(deleteForm.getName());
            bookingOrders.add(bookingOrderByBillId);

            List<HubReceivingEntry> hubReceivingEntries1 = hubReceivingEntryService.getListByBillId(id);
            for (HubReceivingEntry hubReceivingEntry : hubReceivingEntries1) {
                hubReceivingEntry.setBillId(null);
                hubReceivingEntry.setMdyBy(deleteForm.getId().intValue());
                hubReceivingEntry.setMdyByDtm(deleteForm.getDeleteTime());
                hubReceivingEntry.setMdyByName(deleteForm.getName());
                hubReceivingEntries.add(hubReceivingEntry);
            }

            HgBillFollow hgBillFollow = new HgBillFollow();
            hgBillFollow.setBillId(id.intValue());
            hgBillFollow.setSType(OperationEnum.DELETE.getCode());
            hgBillFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            hgBillFollow.setCrtBy(deleteForm.getId().intValue());
            hgBillFollow.setCrtByDtm(deleteForm.getDeleteTime());
            hgBillFollow.setCrtByName(deleteForm.getName());
            hgBillFollows.add(hgBillFollow);
        }

        //去掉委托单中的报关id
        boolean result = bookingOrderService.updateBatchById(bookingOrders);
        if(!result){
            log.warn("修改委托单报关id失败");
        }

        //删除出库明细
        boolean update = hubReceivingEntryService.updateBatchById(hubReceivingEntries);
        if(!update){
            log.warn("删除报关单详情失败");
        }

        boolean b1 = hgBillFollowService.saveBatch(hgBillFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+hgBillFollows);
        }
        return b1;
    }

    @Override
    public boolean entryCustomDate(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgBill hgBill = new HgBill();
        hgBill.setId(form.getId());
        hgBill.setCustomsNo(form.getCustomsNo());
        hgBill.setHkBillNo(form.getHkBillNo());
        LocalDate localDate = LocalDate.parse(form.getCustomsDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime = localDate.atStartOfDay();
        hgBill.setCustomsDate(localDateTime);
        hgBill.setMdyBy(systemUser.getId().intValue());
        hgBill.setMdyByDtm(LocalDateTime.now());
        hgBill.setMdyByName(systemUser.getUserName());

        boolean update = this.updateById(hgBill);
        if(update){
            HgBillFollow hgBillFollow = new HgBillFollow();
            hgBillFollow.setBillId(hgBill.getId());
            hgBillFollow.setSType(OperationEnum.UPDATE.getCode());
            hgBillFollow.setFollowContext("报关日期录入");
            hgBillFollow.setCrtBy(systemUser.getId().intValue());
            hgBillFollow.setCrtByDtm(LocalDateTime.now());
            hgBillFollow.setCrtByName(systemUser.getUserName());
            boolean save = hgBillFollowService.save(hgBillFollow);
            if(save){
                log.warn("报关单号日期录入，操作记录添加成功");
            }
        }

        return update;
    }

    @Override
    public Integer addHgBill(Integer bookingId) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        BookingOrderVO bookingOrderById = bookingOrderService.getBookingOrderById(bookingId);
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderById.getBookingOrderEntryList();
        BigDecimal totalGw = new BigDecimal(0);
        BigDecimal totalNw = new BigDecimal(0);
        BigDecimal totalCbm = new BigDecimal(0);
        Integer packages = 0;
        for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryList) {
            packages = packages + (bookingOrderEntryVO.getPackages()!=null ?bookingOrderEntryVO.getPackages():new BigDecimal(0)).intValue();
            totalGw = totalGw.add(bookingOrderEntryVO.getGw()!=null ?bookingOrderEntryVO.getGw():new BigDecimal(0));
            totalNw = totalNw.add(bookingOrderEntryVO.getNw()!=null ?bookingOrderEntryVO.getNw():new BigDecimal(0));
            totalCbm = totalCbm.add(bookingOrderEntryVO.getCbm()!=null ?bookingOrderEntryVO.getCbm():new BigDecimal(0));
        }

        HgBill hgBill = ConvertUtil.convert(bookingOrderById, HgBill.class);

        hgBill.setTotalCbm(totalCbm);
        hgBill.setTotalGw(totalGw);
        hgBill.setTotalNw(totalNw);
        hgBill.setPackages(packages);
        hgBill.setCrtBy(systemUser.getId().intValue());
        hgBill.setCrtByDtm(LocalDateTime.now());
        hgBill.setCrtByName(systemUser.getUserName());
        hgBill.setCheckStateFlag("N0");
        hgBill.setFStep(0);
        hgBill.setFLevel(2);
        hgBill.setBillDate(LocalDateTime.now());
        hgBill.setBillNo(bookingOrderById.getContractNo());
        hgBill.setCustomerBillNo(bookingOrderById.getContractNo());

        boolean save = this.save(hgBill);
        if(save){
            log.warn("新增报关单成功");

            List<HubReceivingEntry> hubReceivingEntries = hubReceivingEntryService.getListByBookingId(bookingId);
            for (HubReceivingEntry hubReceivingEntry : hubReceivingEntries) {
                hubReceivingEntry.setBillId(hgBill.getId());
            }
            boolean result = this.hubReceivingEntryService.saveOrUpdateBatch(hubReceivingEntries);
            if(result){
                log.warn("新增入库明细成功");
            }

            HgBillFollow hgBillFollow = new HgBillFollow();
            hgBillFollow.setBillId(hgBill.getId());
            hgBillFollow.setSType(OperationEnum.INSERT.getCode());
            hgBillFollow.setFollowContext("委托单入库完成，生成报关单");
            hgBillFollow.setCrtBy(systemUser.getId().intValue());
            hgBillFollow.setCrtByDtm(LocalDateTime.now());
            hgBillFollow.setCrtByName(systemUser.getUserName());
            boolean save1 = hgBillFollowService.save(hgBillFollow);
            if(save1){
                log.warn("新增报关单，操作记录添加成功");
            }
        }
        return hgBill.getId();
    }

    @Override
    public List<SingleWindowData> getSingleWindowData(QueryCommonForm form) {
        List<SingleWindowData> singleWindowData = this.baseMapper.getSingleWindowData(form.getBillNo());
        return singleWindowData;
    }

    @Override
    public List<HgBillVO> getHgBillDataByDeclareState(QueryCommonForm form) {
        QueryWrapper<HgBill> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HgBill::getDeclareState,form.getDeclareState());
        queryWrapper.lambda().eq(HgBill::getVoided,0);
        List<HgBill> hgBills = this.list(queryWrapper);
        List<HgBillVO> hgBillVOS = ConvertUtil.convertList(hgBills, HgBillVO.class);
        return hgBillVOS;
    }

    @Override
    public boolean submitSingleWindow(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgBill hgBill = new HgBill();
        hgBill.setId(form.getId());
        hgBill.setDeclareState(1);
        boolean result = this.updateById(hgBill);
        if(result){
            log.warn("提交单一窗口成功");
            HgBillFollow hgBillFollow = new HgBillFollow();
            hgBillFollow.setBillId(hgBill.getId());
            hgBillFollow.setSType(OperationEnum.UPDATE.getCode());
            hgBillFollow.setFollowContext("提交单一窗口");
            hgBillFollow.setCrtBy(systemUser.getId().intValue());
            hgBillFollow.setCrtByDtm(LocalDateTime.now());
            hgBillFollow.setCrtByName(systemUser.getUserName());
            boolean save1 = hgBillFollowService.save(hgBillFollow);
            if(save1){
                log.warn("提交单一窗口，操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean updateHgBill(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        boolean result = false;
        HgBillFollow hgBillFollow = new HgBillFollow();
        if(form.getDeclareState() != null && form.getDeclareState().equals(2)){
            QueryWrapper<HgBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(HgBill::getBillNo,form.getBillNo());
            queryWrapper.lambda().eq(HgBill::getVoided,0);
            HgBill hgBill = this.getOne(queryWrapper);
            hgBill.setDeclareState(form.getDeclareState());
            hgBill.setSeqNo(form.getSeqNo());
            result = this.updateById(hgBill);
            if(result){
                log.warn("修改状态为'提交成功'成功");
                hgBillFollow.setBillId(hgBill.getId());
                hgBillFollow.setSType(OperationEnum.UPDATE.getCode());
                hgBillFollow.setFollowContext("修改状态为'提交成功'，关联关检号为"+hgBill.getSeqNo());
            }
        }
        if(form.getDeclareState() != null && form.getDeclareState().equals(3)){
            QueryWrapper<HgBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(HgBill::getSeqNo,form.getSeqNo());
            queryWrapper.lambda().eq(HgBill::getVoided,0);
            HgBill hgBill = this.getOne(queryWrapper);
            hgBill.setDeclareState(form.getDeclareState());
            hgBill.setCustomsNo(form.getCustomsNo());
            hgBill.setCustomsDate(DateUtil.stringToLocalDateTime(form.getCustomsDate(),"yyyy-MM-dd HH:mm:ss"));
            result = this.updateById(hgBill);

            if(result){
                log.warn("修改状态为'生成成功'成功");
                hgBillFollow.setBillId(hgBill.getId());
                hgBillFollow.setSType(OperationEnum.UPDATE.getCode());
                hgBillFollow.setFollowContext("修改状态为'生成成功'，录入报关日期和报关单号");
            }

        }
        if(result){

//            hgBillFollow.setCrtBy(systemUser.getId().intValue());
//            hgBillFollow.setCrtByDtm(LocalDateTime.now());
//            hgBillFollow.setCrtByName(systemUser.getUserName());
            boolean save1 = hgBillFollowService.save(hgBillFollow);
            if(save1){
                log.warn("操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean submitYunBaoGuan(HgBill hgBill) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        boolean result = this.updateById(hgBill);
        if(result){
            log.warn("提交云报关成功");
            HgBillFollow hgBillFollow = new HgBillFollow();
            hgBillFollow.setBillId(hgBill.getId());
            hgBillFollow.setSType(OperationEnum.UPDATE.getCode());
            hgBillFollow.setFollowContext("提交云报关");
            hgBillFollow.setCrtBy(systemUser.getId().intValue());
            hgBillFollow.setCrtByDtm(LocalDateTime.now());
            hgBillFollow.setCrtByName(systemUser.getUserName());
            boolean save1 = hgBillFollowService.save(hgBillFollow);
            if(save1){
                log.warn("提交云报关，操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public List<YunBaoGuanData> getYunBaoGuanData(Integer id) {
        List<YunBaoGuanData> yunBaoGuanData = this.baseMapper.getYunBaoGuanData(id);
        return yunBaoGuanData;
    }

    @Override
    public List<HgBill> getHgBillDataByCustomsState() {
        QueryWrapper<HgBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HgBill::getCustomsState,1);
        queryWrapper.lambda().eq(HgBill::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public boolean updateHgBillByYunBaoGuan(HgBill hgBill) {

        boolean result = this.updateById(hgBill);
        if(result){
            log.warn("抓取报关单号和报关日期，并保存成功");
            HgBillFollow hgBillFollow = new HgBillFollow();
            hgBillFollow.setBillId(hgBill.getId());
            hgBillFollow.setSType("系统");
            hgBillFollow.setFollowContext("抓取报关单号和报关日期，修改报关单");
            hgBillFollow.setCrtByDtm(LocalDateTime.now());
            hgBillFollow.setCrtByName("系统");
            boolean save1 = hgBillFollowService.save(hgBillFollow);
            if(save1){
                log.warn("抓取报关单号和报关日期，操作记录添加成功");
            }
        }
        return result;
    }

}
