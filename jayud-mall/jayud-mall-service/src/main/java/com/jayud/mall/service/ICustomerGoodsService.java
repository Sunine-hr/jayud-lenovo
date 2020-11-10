package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.po.CustomerGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CustomerGoodsVO;

/**
 * <p>
 * 客户商品表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface ICustomerGoodsService extends IService<CustomerGoods> {

    /**
     * 分页
     * @param form
     * @return
     */
    IPage<CustomerGoodsVO> findCustomerGoodsByPage(QueryCustomerGoodsForm form);
}
