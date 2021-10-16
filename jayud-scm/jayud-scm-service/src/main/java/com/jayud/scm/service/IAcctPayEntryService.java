package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddAcctPayEntryForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.po.AcctPayEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 应付款表（付款单明细表） 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IAcctPayEntryService extends IService<AcctPayEntry> {

    List<AcctPayEntry> getListByAcctPayId(Long id);

    boolean saveOrUpdateAcctPay(AddAcctPayEntryForm form);

    boolean deleteAcctPayEntry(DeleteForm form);

    List<AcctPayEntry> getAcctPayEntryByOrderId(Integer id);
}
