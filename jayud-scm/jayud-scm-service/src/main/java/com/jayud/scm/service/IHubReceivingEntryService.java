package com.jayud.scm.service;

import com.jayud.scm.model.po.HubReceivingEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IHubReceivingEntryService extends IService<HubReceivingEntry> {

    List<HubReceivingEntry> getShippingEntryByShippingId(Long id);
}
