package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryShippingAreaForm;
import com.jayud.mall.model.po.ShippingArea;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 集货仓表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IShippingAreaService extends IService<ShippingArea> {

    /**
     * 分页查询集货仓
     * @param form
     * @return
     */
    IPage<ShippingArea> findShippingAreaByPage(QueryShippingAreaForm form);
}
