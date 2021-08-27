package com.jayud.scm.service.impl;

import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.HubReceivingMapper;
import com.jayud.scm.model.vo.HubReceivingEntryVO;
import com.jayud.scm.model.vo.HubReceivingVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 入库主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class HubReceivingServiceImpl extends ServiceImpl<HubReceivingMapper, HubReceiving> implements IHubReceivingService {

    @Autowired
    private IHubReceivingEntryService hubReceivingEntryService;

    @Autowired
    private IHubReceivingFollowService hubReceivingFollowService;

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private ICheckOrderEntryService checkOrderEntryService;

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<HubReceivingEntry> hubReceivingEntries = new ArrayList<>();
        List<HubReceivingFollow> hubReceivingFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            List<HubReceivingEntry> hubShippingEntries1 = hubReceivingEntryService.getReceivingEntryByReceivingId(id);
            for (HubReceivingEntry hubReceivingEntry : hubShippingEntries1) {
                hubReceivingEntry.setVoidedBy(deleteForm.getId().intValue());
                hubReceivingEntry.setVoidedByDtm(deleteForm.getDeleteTime());
                hubReceivingEntry.setVoidedByName(deleteForm.getName());
                hubReceivingEntry.setVoided(0);
                hubReceivingEntries.add(hubReceivingEntry);
            }

            HubReceivingFollow hubReceivingFollow = new HubReceivingFollow();
            hubReceivingFollow.setReceivingId(id.intValue());
            hubReceivingFollow.setSType(OperationEnum.DELETE.getCode());
            hubReceivingFollow.setFollowContext("删除入库订单"+OperationEnum.DELETE.getDesc()+id);
            hubReceivingFollow.setCrtBy(deleteForm.getId().intValue());
            hubReceivingFollow.setCrtByDtm(deleteForm.getDeleteTime());
            hubReceivingFollow.setCrtByName(deleteForm.getName());
            hubReceivingFollows.add(hubReceivingFollow);
        }
        //删除出库明细
        boolean update = hubReceivingEntryService.updateBatchById(hubReceivingEntries);
        if(!update){
            log.warn("删除出库订单详情失败");
        }

        boolean b1 = hubReceivingFollowService.saveBatch(hubReceivingFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+hubReceivingFollows);
        }
        return b1;
    }

    @Override
    public HubReceivingVO getHubReceivingById(Integer id) {
        HubReceiving hubReceiving = this.getById(id);
        List<HubReceivingEntry> hubShippingEntries = hubReceivingEntryService.getReceivingEntryByReceivingId(hubReceiving.getId().longValue());
        HubReceivingVO hubReceivingVO = ConvertUtil.convert(hubReceiving, HubReceivingVO.class);
        List<HubReceivingEntryVO> hubReceivingEntries = ConvertUtil.convertList(hubShippingEntries, HubReceivingEntryVO.class);
        hubReceivingVO.setHubReceivingEntryVOS(hubReceivingEntries);
        return hubReceivingVO;
    }

    @Override
    public boolean addHubReceiving(Integer id) {
        CheckOrder checkOrder = checkOrderService.getById(id);
        checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrder.getId().longValue());
        return false;
    }

}
