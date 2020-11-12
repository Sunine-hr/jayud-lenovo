package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.po.OceanCounter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 提单对应货柜信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOceanCounterService extends IService<OceanCounter> {

    /**
     * 保存-提单对应货柜信息
     * @param form
     * @return
     */
    CommonResult saveOceanCounter(OceanCounterForm form);
}
