package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddHgTruckFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgTruckFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HgTruckFollowVO;

/**
 * <p>
 * 港车跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
public interface IHgTruckFollowService extends IService<HgTruckFollow> {

    IPage<HgTruckFollowVO> findListByHgTruckId(QueryCommonForm form);

    boolean addHgTruckFollow(AddHgTruckFollowForm followForm);
}
