package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctReceiptFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;

/**
 * <p>
 * 付款跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IAcctReceiptFollowService extends IService<AcctReceiptFollow> {

    IPage<AcctReceiptFollowVO> findListByAcctReceiptId(QueryCommonForm form);
}
