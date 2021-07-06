package com.jayud.mall.service;

import com.jayud.mall.model.po.IntoWarehouseNumber;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 进仓单号表(取单号) 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-01
 */
public interface IIntoWarehouseNumberService extends IService<IntoWarehouseNumber> {

    /**
     * 获取进仓单号
     * @return
     */
    String getWarehouseNo();

}
