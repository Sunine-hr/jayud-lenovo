package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.SupplierCostForm;
import com.jayud.mall.model.po.SupplierCost;
import com.jayud.mall.model.vo.SupplierCostVO;

import java.util.List;

/**
 * <p>
 * 供应商服务费用 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
public interface ISupplierCostService extends IService<SupplierCost> {


    /**
     * 查询-供应商服务费用-list
     * @param form
     * @return
     */
    List<SupplierCostVO> findSupplierCost(SupplierCostForm form);

}
