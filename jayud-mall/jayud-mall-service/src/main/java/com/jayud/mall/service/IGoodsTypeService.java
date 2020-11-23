package com.jayud.mall.service;

import com.jayud.mall.model.bo.GoodsTypeForm;
import com.jayud.mall.model.po.GoodsType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * (报价&货物)类型表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IGoodsTypeService extends IService<GoodsType> {

    /**
     * 查询(报价&货物)类型表list
     * @param form
     * @return
     */
    List<GoodsType> findGoodsType(GoodsTypeForm form);
}
