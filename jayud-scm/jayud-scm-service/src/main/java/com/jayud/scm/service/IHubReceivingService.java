package com.jayud.scm.service;

import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubReceiving;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HubReceivingVO;

/**
 * <p>
 * 入库主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IHubReceivingService extends IService<HubReceiving> {

    boolean delete(DeleteForm deleteForm);

    HubReceivingVO getHubReceivingById(Integer id);

    boolean addHubReceiving(QueryCommonForm form);

    boolean deleteHubReceiving(Integer id);
}
