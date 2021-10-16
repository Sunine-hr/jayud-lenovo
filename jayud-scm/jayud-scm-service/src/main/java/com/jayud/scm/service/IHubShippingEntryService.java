package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddHubShippingEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubReceivingEntry;
import com.jayud.scm.model.po.HubShippingEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HubShippingEntryVO;

import java.util.List;

/**
 * <p>
 * 出库单明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IHubShippingEntryService extends IService<HubShippingEntry> {

    List<HubShippingEntry> getShippingEntryByShippingId(Long id);

    IPage<HubShippingEntryVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateHubShippingEntry(List<AddHubShippingEntryForm> form);

    List<HubShippingEntry> getShippingEntryByBookingEntryId(Integer id);
}
