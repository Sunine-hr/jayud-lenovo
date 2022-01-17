package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctPayFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.AcctPayFollowVO;

/**
 * <p>
 * 付款跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IAcctPayFollowService extends IService<AcctPayFollow> {

    IPage<AcctPayFollowVO> findListByAcctPayId(QueryCommonForm form);
}
