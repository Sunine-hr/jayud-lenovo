package com.jayud.scm.service;

import com.jayud.scm.model.po.TaxInvoice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 销项票主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
public interface ITaxInvoiceService extends IService<TaxInvoice> {

    boolean dispatch(List<Integer> taxInvoiceIds, Integer id, String deliverNo);

    boolean deleteDispatch(List<Integer> taxInvoiceIds);

    List<TaxInvoice> getTaxInvoiceByDeliverId(int intValue);
}
