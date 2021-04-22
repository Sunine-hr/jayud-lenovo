package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryPromoteOrderForm;
import com.jayud.mall.model.bo.SavePromoteOrderForm;
import com.jayud.mall.model.po.PromoteOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.PromoteOrderVO;

/**
 * <p>
 * 推广订单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
public interface IPromoteOrderService extends IService<PromoteOrder> {

    IPage<PromoteOrderVO> findPromoteOrderByPage(QueryPromoteOrderForm form);

    void savePromoteOrder(SavePromoteOrderForm form);

    PromoteOrderVO findPromoteOrderById(Integer id);
}
