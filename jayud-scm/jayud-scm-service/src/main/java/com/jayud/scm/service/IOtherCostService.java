package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddOtherCostForm;
import com.jayud.scm.model.po.OtherCost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 其他费用记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
public interface IOtherCostService extends IService<OtherCost> {

    boolean saveOrUpdateOtherCost(AddOtherCostForm form);
}
