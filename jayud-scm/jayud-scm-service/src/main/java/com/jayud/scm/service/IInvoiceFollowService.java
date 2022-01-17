package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.InvoiceFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.InvoiceFollowVO;

/**
 * <p>
 * 结算单（应收款）跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IInvoiceFollowService extends IService<InvoiceFollow> {

    IPage<InvoiceFollowVO> findListByInvoiceId(QueryCommonForm form);
}
