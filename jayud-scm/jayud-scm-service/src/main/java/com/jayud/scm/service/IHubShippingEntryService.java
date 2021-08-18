package com.jayud.scm.service;

import com.jayud.scm.model.po.HubShippingEntry;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
