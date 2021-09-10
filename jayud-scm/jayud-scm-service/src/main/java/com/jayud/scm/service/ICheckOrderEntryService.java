package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrderEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CheckOrderEntryVO;

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

    IPage<CheckOrderEntryVO> findByPage(QueryCommonForm form);

    List<CheckOrderEntry> getCheckOrderEntryByBookingId(Integer bookingId);
}
