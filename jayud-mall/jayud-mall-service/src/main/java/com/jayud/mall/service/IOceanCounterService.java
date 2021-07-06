package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.vo.OceanCounterVO;

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
     * 根据id查询
     * @param id
     * @return
     */
    OceanCounterVO findOceanCounterById(Long id);
}
