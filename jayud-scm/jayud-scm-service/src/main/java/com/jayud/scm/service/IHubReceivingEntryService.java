package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubReceivingEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HubReceivingEntryVO;

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

    IPage<HubReceivingEntryVO> findByPage(QueryCommonForm form);
}
