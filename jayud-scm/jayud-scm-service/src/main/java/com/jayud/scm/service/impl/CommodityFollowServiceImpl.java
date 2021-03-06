package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCommodityFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CommodityFollow;
import com.jayud.scm.mapper.CommodityFollowMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.service.ICommodityFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品操作日志表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class CommodityFollowServiceImpl extends ServiceImpl<CommodityFollowMapper, CommodityFollow> implements ICommodityFollowService {

    @Autowired
    private ISystemUserService systemUserService;

    /**
     * 根据商品id获取商品操作日志记录
     * @param form
     * @return
     */
    @Override
    public IPage<CommodityFollowVO> findListByCommodityId(QueryCommonForm form) {
        Page<CommodityFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByCommodityId(form,page);
    }

    @Override
    public boolean AddCommodityFollow(AddCommodityFollowForm followForm) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        CommodityFollow commodityFollow = ConvertUtil.convert(followForm, CommodityFollow.class);
        commodityFollow.setSType(OperationEnum.INSERT.getCode());
        commodityFollow.setCrtBy(systemUser.getId().intValue());
        commodityFollow.setCrtByDtm(LocalDateTime.now());
        commodityFollow.setCrtByName(systemUser.getUserName());

        boolean save = this.save(commodityFollow);
        if(!save){
            log.warn("商品操作日志添加失败"+commodityFollow);
            return false;
        }
        return true;
    }
}
