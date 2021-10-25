package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.Invoice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.InvoiceDetailVO;

import java.util.List;

/**
 * <p>
 * 结算单（应收款）主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IInvoiceService extends IService<Invoice> {

    IPage<InvoiceDetailVO> findByPage(QueryForm form);

    boolean delete(DeleteForm deleteForm);

    List<Invoice> getByOrderId(Integer id);
}
