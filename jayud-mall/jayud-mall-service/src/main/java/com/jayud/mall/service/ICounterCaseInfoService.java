package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BatchCounterCaseInfoForm;
import com.jayud.mall.model.bo.OrderCaseQueryForm;
import com.jayud.mall.model.po.CounterCaseInfo;
import com.jayud.mall.model.vo.OrderCaseVO;

import java.util.List;

/**
 * <p>
 * 柜子箱号信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
public interface ICounterCaseInfoService extends IService<CounterCaseInfo> {

    /**
     * 查询-未选择的箱子(运单-绑定装柜箱子)
     * @param form
     * @return
     */
    List<OrderCaseVO> findUnselectedOrderCase(OrderCaseQueryForm form);

    /**
     * 查询-已选择的箱子(运单-绑定装柜箱子)
     * @param form
     * @return
     */
    List<OrderCaseVO> findSelectedOrderCase(OrderCaseQueryForm form);

    /**
     * 批量移入(运单-绑定装柜箱子)
     * @param form
     */
    void batchIntoCounterCaseInfo(BatchCounterCaseInfoForm form);

    /**
     * 批量移除(运单-绑定装柜箱子)
     * @param form
     */
    void batchRemoveCounterCaseInfo(BatchCounterCaseInfoForm form);
}
