package com.jayud.mall.service;

import com.jayud.mall.model.bo.CostItemForm;
import com.jayud.mall.model.bo.CostItemSupForm;
import com.jayud.mall.model.po.CostItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CostItemVO;

import java.util.List;

/**
 * <p>
 * 费用项目 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-28
 */
public interface ICostItemService extends IService<CostItem> {

    /**
     * 查询费用项目list
     * @param form
     * @return
     */
    List<CostItemVO> findCostItem(CostItemForm form);

    /**
     * 根据供应商id，获取供应商费用（应付费用信息）
     * @param form
     * @return
     */
    List<CostItemVO> findCostItemBySupId(CostItemSupForm form);
}
