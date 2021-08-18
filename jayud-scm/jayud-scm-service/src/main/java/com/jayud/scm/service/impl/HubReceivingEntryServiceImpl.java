package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.HubReceivingEntry;
import com.jayud.scm.mapper.HubReceivingEntryMapper;
import com.jayud.scm.service.IHubReceivingEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 入库明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class HubReceivingEntryServiceImpl extends ServiceImpl<HubReceivingEntryMapper, HubReceivingEntry> implements IHubReceivingEntryService {

    @Override
    public List<HubReceivingEntry> getShippingEntryByShippingId(Long id) {
        QueryWrapper<HubReceivingEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HubReceivingEntry::getReceivingId,id);
        queryWrapper.lambda().eq(HubReceivingEntry::getVoided,0);
        return this.list(queryWrapper);
    }
}
