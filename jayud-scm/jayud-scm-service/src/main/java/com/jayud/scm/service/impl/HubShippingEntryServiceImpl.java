package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.HubShippingEntry;
import com.jayud.scm.mapper.HubShippingEntryMapper;
import com.jayud.scm.service.IHubShippingEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 出库单明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class HubShippingEntryServiceImpl extends ServiceImpl<HubShippingEntryMapper, HubShippingEntry> implements IHubShippingEntryService {

    @Override
    public List<HubShippingEntry> getShippingEntryByShippingId(Long id) {
        QueryWrapper<HubShippingEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HubShippingEntry::getShippingId,id);
        queryWrapper.lambda().eq(HubShippingEntry::getVoided,0);
        return this.list(queryWrapper);
    }
}
