package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.po.Commodity;
import com.jayud.scm.model.po.CommodityEntry;
import com.jayud.scm.mapper.CommodityEntryMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CommodityEntryVO;
import com.jayud.scm.service.ICommodityEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品申报要素明细表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class CommodityEntryServiceImpl extends ServiceImpl<CommodityEntryMapper, CommodityEntry> implements ICommodityEntryService {

    @Autowired
    private ISystemUserService systemUserService;

    /**
     * 根据商品id获取商品要素信息
     * @param id
     * @return
     */
    @Override
    public List<CommodityEntryVO> getCommodityEntry(Integer id) {
        QueryWrapper<CommodityEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CommodityEntry::getCommodityId,id);
        queryWrapper.lambda().eq(CommodityEntry::getVoided,0);
        List<CommodityEntry> list = this.list(queryWrapper);
        return ConvertUtil.convertList(list,CommodityEntryVO.class);
    }

    @Override
    public boolean delete(Integer id) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        QueryWrapper<CommodityEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CommodityEntry::getCommodityId,id);
        queryWrapper.lambda().eq(CommodityEntry::getVoided,0);
        List<CommodityEntry> list = this.list(queryWrapper);
        for (CommodityEntry commodityEntry : list) {
            commodityEntry.setVoided(1);
            commodityEntry.setVoidedBy(systemUser.getId().intValue());
            commodityEntry.setVoidedByDtm(LocalDateTime.now());
            commodityEntry.setVoidedByName(UserOperator.getToken());
        }
        return this.updateBatchById(list);
    }
}
