package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AccountBankBillFollow;
import com.jayud.scm.mapper.AccountBankBillFollowMapper;
import com.jayud.scm.model.vo.AccountBankBillFollowVO;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.service.IAccountBankBillFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 水单跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Service
public class AccountBankBillFollowServiceImpl extends ServiceImpl<AccountBankBillFollowMapper, AccountBankBillFollow> implements IAccountBankBillFollowService {

    @Override
    public IPage<AccountBankBillFollowVO> findListByAccountBankBillId(QueryCommonForm form) {
        Page<AccountBankBillFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByAccountBankBillId(form,page);
    }
}
