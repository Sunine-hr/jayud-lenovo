package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AccountBankBillFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.AccountBankBillFollowVO;

/**
 * <p>
 * 水单跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
public interface IAccountBankBillFollowService extends IService<AccountBankBillFollow> {

    IPage<AccountBankBillFollowVO> findListByAccountBankBillId(QueryCommonForm form);
}
