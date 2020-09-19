package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.CostInfo;

import java.util.List;

/**
 * <p>
 * 费用名描述 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
public interface ICostInfoService extends IService<CostInfo> {

    /**
     * 获取费用类型
     * @return
     */
    List<CostInfo> findCostInfo();

}
