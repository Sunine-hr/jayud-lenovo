package com.jayud.scm.service.impl;

import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.CheckOrderMapper;
import com.jayud.scm.service.ICheckOrderEntryService;
import com.jayud.scm.service.ICheckOrderFollowService;
import com.jayud.scm.service.ICheckOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                checkOrderEntry.setVoided(0);
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
}
