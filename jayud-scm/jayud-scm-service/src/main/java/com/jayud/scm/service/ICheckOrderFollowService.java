package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrderFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CheckOrderFollowVO;

/**
 * <p>
 * 提验货单跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
public interface ICheckOrderFollowService extends IService<CheckOrderFollow> {

    IPage<CheckOrderFollowVO> findListByCheckOrderId(QueryCommonForm form);
}
