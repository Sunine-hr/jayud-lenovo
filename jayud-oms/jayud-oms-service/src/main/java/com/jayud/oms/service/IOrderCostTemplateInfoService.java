package com.jayud.oms.service;

import com.jayud.oms.model.po.OrderCostTemplateInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 费用模板详情 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
public interface IOrderCostTemplateInfoService extends IService<OrderCostTemplateInfo> {


    /**
     * 根据模板id删除数据
     */
    public int deleteByTemplateId(Long templateId);
}
