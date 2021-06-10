package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryOrderCaseWmsForm;
import com.jayud.mall.model.po.OrderCaseWms;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderCaseWmsVO;

/**
 * <p>
 * 订单装箱信息(仓库测量) 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-09
 */
public interface IOrderCaseWmsService extends IService<OrderCaseWms> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OrderCaseWmsVO> findOrderCaseWmsPage(QueryOrderCaseWmsForm form);

    /**
     * 根据箱号查询
     * @param cartonNo
     * @return
     */
    OrderCaseWmsVO findOrderCaseWmsByCartonNo(String cartonNo);
}
