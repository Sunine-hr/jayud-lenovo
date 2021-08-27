package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddHubShippingForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubShipping;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HubShippingVO;

import java.util.List;

/**
 * <p>
 * 出库单主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IHubShippingService extends IService<HubShipping> {

    boolean delete(DeleteForm deleteForm);

    HubShippingVO getHubShippingById(Integer id);

    boolean signOrder(QueryCommonForm form);

    boolean saveOrUpdateHubShipping(AddHubShippingForm form);

    List<HubShippingVO> getHubShippingByBookingId(QueryCommonForm form);
}
