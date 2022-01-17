package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddHubShippingFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubShippingFollow;
import com.jayud.scm.mapper.HubShippingFollowMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.HubShippingEntryVO;
import com.jayud.scm.model.vo.HubShippingFollowVO;
import com.jayud.scm.service.IHubShippingFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.time.LocalDateTime;

/**
 * <p>
 * 出库单跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Service
public class HubShippingFollowServiceImpl extends ServiceImpl<HubShippingFollowMapper, HubShippingFollow> implements IHubShippingFollowService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public IPage<HubShippingFollowVO> findListByHubShippingId(QueryCommonForm form) {
        Page<HubShippingFollowVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean addHubShippingFollow(AddHubShippingFollowForm followForm) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        HubShippingFollow hubShippingFollow = ConvertUtil.convert(followForm, HubShippingFollow.class);
        hubShippingFollow.setCrtBy(systemUser.getId().intValue());
        hubShippingFollow.setCrtByDtm(LocalDateTime.now());
        hubShippingFollow.setCrtByName(systemUser.getUserName());

        boolean save = this.save(hubShippingFollow);
        if(!save){
            log.warn("添加日志记录成功");
        }
        return save;
    }
}
