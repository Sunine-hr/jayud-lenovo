package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.*;
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

    /**
     * 分页
     * @param form
     * @return
     */
    IPage<CostItemVO> findCostItemByPage(QueryCostItemForm form);

    /**
     * 保存
     * @param form
     * @return
     */
    CostItemVO saveCostItem(CostItemForm form);

    /**
     * 停用启用
     * @param form
     */
    void stopOrEnabled(CostItemStatusForm form);

    /**
     * 根据供应商服务id，获取供应商费用（应付费用信息）
     * @param form
     * @return
     */
    List<CostItemVO> findCostItemByServiceId(SupplierCostServerIdForm form);
}
