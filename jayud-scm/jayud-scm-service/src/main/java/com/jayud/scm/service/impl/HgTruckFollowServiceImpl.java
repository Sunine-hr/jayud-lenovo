package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHgTruckFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgTruckFollow;
import com.jayud.scm.mapper.HgTruckFollowMapper;
import com.jayud.scm.model.po.HubShippingFollow;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.HgTruckFollowVO;
import com.jayud.scm.model.vo.HubShippingFollowVO;
import com.jayud.scm.service.IHgTruckFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 港车跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Service
public class HgTruckFollowServiceImpl extends ServiceImpl<HgTruckFollowMapper, HgTruckFollow> implements IHgTruckFollowService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public IPage<HgTruckFollowVO> findListByHgTruckId(QueryCommonForm form) {
        Page<HubShippingFollowVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean addHgTruckFollow(AddHgTruckFollowForm followForm) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HgTruckFollow hgTruckFollow = ConvertUtil.convert(followForm, HgTruckFollow.class);
        hgTruckFollow.setCrtBy(systemUser.getId().intValue());
        hgTruckFollow.setCrtByDtm(LocalDateTime.now());
        hgTruckFollow.setCrtByName(systemUser.getUserName());

        boolean save = this.save(hgTruckFollow);
        if(!save){
            log.warn("港车添加日志记录成功");
        }
        return save;
    }


}
