package com.jayud.mall.service;

import com.jayud.mall.model.po.NumberGenerated;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 单号生成器 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-21
 */
public interface INumberGeneratedService extends IService<NumberGenerated> {

    /**
     * 根据`单号编号code`，获取生成的`单号`
     * @return
     */
    String getOrderNoByCode(String code);
}
