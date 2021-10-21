package com.jayud.finance.service;

import cn.hutool.json.JSONArray;
import com.jayud.finance.bo.QueryProfitStatementForm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.vo.ProfitStatementBillVO;
import com.jayud.finance.vo.ProfitStatementVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @param rollCallback
     * @return
     */
    List<ProfitStatementVO> list(QueryProfitStatementForm form, Map<String, Object> rollCallback);


    ProfitStatementBillVO getProfitStatementBill(List<String> reCostIds, List<String> payCostIds);


    /**
     * 导出利润报表
     * @param jsonArray
     * @param isOpenInternal
     */
    void exportData(JSONArray jsonArray, Boolean isOpenInternal) throws IOException;
}
