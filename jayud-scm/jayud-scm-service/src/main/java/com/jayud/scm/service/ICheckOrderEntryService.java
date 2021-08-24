package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.po.CheckOrderEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 提验货单明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
public interface ICheckOrderEntryService extends IService<CheckOrderEntry> {

    List<CheckOrderEntry> getCheckOrderEntryByCheckOrderId(Long id);

    boolean addCheckOrderEntry(List<AddCheckOrderEntryForm> form);

    boolean updateCheckOrderEntry(List<AddCheckOrderEntryForm> form);
}
