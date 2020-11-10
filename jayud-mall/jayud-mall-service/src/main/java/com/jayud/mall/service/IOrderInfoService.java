package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderInfoVO;

/**
 * <p>
 * 产品订单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form);
}
