package com.jayud.oms.service;

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
    public Integer auditPendingExpenses(String subType, List<Long> legalIds);
}
