package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HgBillMapper;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.model.vo.HgBillVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hdgf.HDGFDiagram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for (Long id : deleteForm.getIds()) {

            List<HubReceivingEntry> hubReceivingEntries1 = hubReceivingEntryService.getListByBillId(id);
            for (HubReceivingEntry hubReceivingEntry : hubReceivingEntries1) {
                hubReceivingEntry.setVoidedBy(deleteForm.getId().intValue());
                hubReceivingEntry.setVoidedByDtm(deleteForm.getDeleteTime());
                hubReceivingEntry.setVoidedByName(deleteForm.getName());
                hubReceivingEntry.setVoided(1);
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
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        hgBill.setCustomsDate(LocalDateTime.parse(form.getCustomsDate(),df));
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

        HgBill hgBill = ConvertUtil.convert(bookingOrderById, HgBill.class);
        hgBill.setCrtBy(systemUser.getId().intValue());
        hgBill.setCrtByDtm(LocalDateTime.now());
        hgBill.setCrtByName(systemUser.getUserName());
        hgBill.setCheckStateFlag("N0");
        hgBill.setCustomsNo(bookingOrderById.getContractNo());
        hgBill.setBillDate(LocalDateTime.now());
        hgBill.setBillNo(commodityService.getOrderNo(NoCodeEnum.HG_BILL.getCode(),LocalDateTime.now()));
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

}
