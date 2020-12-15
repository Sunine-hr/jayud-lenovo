package com.jayud.mall.service;

import com.jayud.mall.model.bo.OceanCounterWaybillRelationForm;
import com.jayud.mall.model.po.OceanCounterWaybillRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OceanCounterWaybillRelationVO;

import java.util.List;

/**
 * <p>
 * 货柜对应运单(订单)关联表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-15
 */
public interface IOceanCounterWaybillRelationService extends IService<OceanCounterWaybillRelation> {

    /**
     * 保存-货柜对应运单(订单)关联表
     * @param forms
     * @return
     */
    List<OceanCounterWaybillRelationVO> saveOceanCounterWaybillRelation(List<OceanCounterWaybillRelationForm> forms);
}
