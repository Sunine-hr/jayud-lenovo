package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HgBillMapper;
import com.jayud.scm.model.vo.HgBillVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.IHgBillFollowService;
import com.jayud.scm.service.IHgBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.IHubReceivingEntryService;
import com.jayud.scm.service.ISystemUserService;
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
                hubReceivingEntry.setVoided(0);
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
            hgBillFollow.setFollowContext("");
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

}
