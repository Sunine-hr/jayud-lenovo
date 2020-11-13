package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanWaybillCaseRelationForm;
import com.jayud.mall.model.po.OceanWaybillCaseRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 运单对应箱号关联表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
public interface IOceanWaybillCaseRelationService extends IService<OceanWaybillCaseRelation> {

    /**
     * 保存-运单对应箱号接口
     * @param form
     * @return
     */
    CommonResult saveOceanWaybillCaseRelation(OceanWaybillCaseRelationForm form);
}
