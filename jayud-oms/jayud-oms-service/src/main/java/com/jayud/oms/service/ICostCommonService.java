package com.jayud.oms.service;

import com.jayud.common.entity.DataControl;
import com.jayud.oms.model.po.OrderInfo;

import java.util.List;

/**
 * <p>
 * 费用公共服务
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface ICostCommonService {

    /**
     * 统计应收/应付待处理费用审核
     */
    public Integer auditPendingExpenses(String subType, DataControl dataControl, List<String> orderNos, String userName);

    /**
     * 查询所有未录用费用订单数量
     * @param list
     * @param legalIds
     * @param subType
     * @param userName
     * @return
     */
    Integer allUnemployedFeesNum(List<OrderInfo> list, List<Long> legalIds,
                                 String subType, String userName);
}
