package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddHubShippingFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubShippingFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HubShippingFollowVO;

/**
 * <p>
 * 出库单跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IHubShippingFollowService extends IService<HubShippingFollow> {

    IPage<HubShippingFollowVO> findListByHubShippingId(QueryCommonForm form);

    boolean addHubShippingFollow(AddHubShippingFollowForm followForm);
}
