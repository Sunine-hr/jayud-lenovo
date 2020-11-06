package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.po.OrderCase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderCaseVO;

/**
 * <p>
 * 订单对应箱号信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOrderCaseService extends IService<OrderCase> {

    /**
     * 分页
     * @param form
     * @return
     */
    IPage<OrderCaseVO> findOrderCaseByPage(QueryOrderCaseForm form);
}
