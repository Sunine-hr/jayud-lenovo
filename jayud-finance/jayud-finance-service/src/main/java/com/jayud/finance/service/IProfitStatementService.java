package com.jayud.finance.service;

import com.jayud.finance.bo.QueryProfitStatementForm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.vo.ProfitStatementVO;

import java.util.List;

/**
 * <p>
 * 利润报表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
public interface IProfitStatementService extends IService<ProfitStatement> {

    /**
     * 统计利润报表
     */
    List<ProfitStatement> statisticalProfitReport();

    /**
     * 同步数据
     *
     * @param list
     */
    void synchronizeData(List<ProfitStatement> list);

    /**
     * 利润报表查询
     * @param form
     * @return
     */
    List<ProfitStatementVO> list(QueryProfitStatementForm form);
}
