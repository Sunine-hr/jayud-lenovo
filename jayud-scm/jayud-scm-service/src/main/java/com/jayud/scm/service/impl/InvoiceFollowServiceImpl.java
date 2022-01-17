package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.InvoiceFollow;
import com.jayud.scm.mapper.InvoiceFollowMapper;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import com.jayud.scm.model.vo.InvoiceFollowVO;
import com.jayud.scm.service.IInvoiceFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 结算单（应收款）跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class InvoiceFollowServiceImpl extends ServiceImpl<InvoiceFollowMapper, InvoiceFollow> implements IInvoiceFollowService {

    @Override
    public IPage<InvoiceFollowVO> findListByInvoiceId(QueryCommonForm form) {
        Page<InvoiceFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByInvoiceId(form,page);
    }
}
