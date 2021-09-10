package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctReceiptFollow;
import com.jayud.scm.mapper.AcctReceiptFollowMapper;
import com.jayud.scm.model.vo.AccountBankBillFollowVO;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import com.jayud.scm.service.IAcctReceiptFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 付款跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class AcctReceiptFollowServiceImpl extends ServiceImpl<AcctReceiptFollowMapper, AcctReceiptFollow> implements IAcctReceiptFollowService {

    @Override
    public IPage<AcctReceiptFollowVO> findListByAcctReceiptId(QueryCommonForm form) {
        Page<AcctReceiptFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByAccountBankBillId(form,page);
    }
}
