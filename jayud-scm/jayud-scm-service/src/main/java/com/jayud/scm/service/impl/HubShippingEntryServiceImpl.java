package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHubShippingEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubShippingEntry;
import com.jayud.scm.mapper.HubShippingEntryMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.HubReceivingEntryVO;
import com.jayud.scm.model.vo.HubShippingEntryVO;
import com.jayud.scm.service.IHubShippingEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<HubShippingEntry> getShippingEntryByShippingId(Long id) {
        QueryWrapper<HubShippingEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HubShippingEntry::getShippingId,id);
        queryWrapper.lambda().eq(HubShippingEntry::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<HubShippingEntryVO> findByPage(QueryCommonForm form) {
        Page<HubShippingEntryVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateHubShippingEntry(List<AddHubShippingEntryForm> form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<HubShippingEntry> hubShippingEntries = ConvertUtil.convertList(form, HubShippingEntry.class);
        for (HubShippingEntry hubShippingEntry : hubShippingEntries) {
            QueryWrapper<HubShippingEntry> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(HubShippingEntry::getBookingEntryId,hubShippingEntry.getBookingEntryId());
            HubShippingEntry one = this.getOne(queryWrapper);
            if(one != null){
                hubShippingEntry.setMdyBy(systemUser.getId().intValue());
                hubShippingEntry.setMdyByDtm(LocalDateTime.now());
                hubShippingEntry.setMdyByName(systemUser.getUserName());
                hubShippingEntry.setQty(hubShippingEntry.getQty().add(one.getQty()));
                hubShippingEntry.setPackages((hubShippingEntry.getPackages() != null? hubShippingEntry.getPackages():0)+ (one.getPackages()!=null?one.getPackages():0));
                hubShippingEntry.setGw((hubShippingEntry.getGw()!=null?hubShippingEntry.getGw():new BigDecimal(0)).add((one.getGw()!= null?one.getGw():new BigDecimal(0))));
                hubShippingEntry.setNw((hubShippingEntry.getNw()!=null?hubShippingEntry.getNw():new BigDecimal(0)).add((one.getNw()!= null?one.getNw():new BigDecimal(0))));
                hubShippingEntry.setCbm((hubShippingEntry.getCbm()!=null?hubShippingEntry.getCbm():new BigDecimal(0)).add((one.getCbm()!= null?one.getCbm():new BigDecimal(0))));
            }else{
                hubShippingEntry.setCrtBy(systemUser.getId().intValue());
                hubShippingEntry.setCrtByDtm(LocalDateTime.now());
                hubShippingEntry.setCrtByName(systemUser.getUserName());
            }
        }
        boolean b = this.saveOrUpdateBatch(hubShippingEntries);
        if(!b){
            log.warn("出库订单明细添加或修改失败");
        }

        return b;
    }

    @Override
    public List<HubShippingEntry> getShippingEntryByBookingEntryId(Integer id) {
        QueryWrapper<HubShippingEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HubShippingEntry::getBookingEntryId,id);
        queryWrapper.lambda().eq(HubShippingEntry::getVoided,0);
        return this.list(queryWrapper);
    }
}
