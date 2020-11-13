package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanWaybillForm;
import com.jayud.mall.model.po.OceanWaybill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 运单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
public interface IOceanWaybillService extends IService<OceanWaybill> {

    /**
     * 保存-货柜对应运单信息
     * @param form
     * @return
     */
    CommonResult saveOceanWaybill(OceanWaybillForm form);
}
