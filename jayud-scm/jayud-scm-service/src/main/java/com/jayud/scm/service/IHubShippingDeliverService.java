package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddHubShippingDeliverForm;
import com.jayud.scm.model.bo.DispatchForm;
import com.jayud.scm.model.po.HubShippingDeliver;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HubShippingDeliverVO;

/**
 * <p>
 * 调度配送表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-11-12
 */
public interface IHubShippingDeliverService extends IService<HubShippingDeliver> {

    boolean saveOrUpdateHubShippingDeliver(AddHubShippingDeliverForm form);

    HubShippingDeliverVO getHubShippingDeliverById(Integer id);

    boolean shippingDeliverTruckSend(Integer id);

    boolean deliverStatusBack(Integer id);

    boolean dispatch(DispatchForm form);

    boolean deleteDispatch(DispatchForm form);

    HubShippingDeliver saveHubShippingDeliver(HubShippingDeliver hubShippingDeliver);
}
