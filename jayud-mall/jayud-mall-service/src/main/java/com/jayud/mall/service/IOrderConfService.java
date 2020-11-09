package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OrderConf;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderConfVO;

/**
 * <p>
 * 配载单 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface IOrderConfService extends IService<OrderConf> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form);
}
