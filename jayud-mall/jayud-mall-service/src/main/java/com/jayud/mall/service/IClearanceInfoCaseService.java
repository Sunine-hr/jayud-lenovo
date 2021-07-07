package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BillClearanceInfoQueryForm;
import com.jayud.mall.model.bo.CreateClearanceInfoCaseForm;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.jayud.mall.model.vo.BillCaseVO;

import java.util.List;

/**
 * <p>
 * 清关文件箱号 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IClearanceInfoCaseService extends IService<ClearanceInfoCase> {

    /**
     * 清关箱子-查询提单下未生成的订单箱子(分类型)
     * @param form
     * @return
     */
    List<BillCaseVO> findUnselectedBillCaseByClearance(BillClearanceInfoQueryForm form);

    /**
     * 清关箱子-查询提单下已生成的订单箱子
     * @param form
     * @return
     */
    List<BillCaseVO> findSelectedBillCaseByClearance(BillClearanceInfoQueryForm form);

    /**
     * 提单下的清关-生成清关清单
     * @param form
     */
    void createClearanceInfoCase(CreateClearanceInfoCaseForm form);
}
