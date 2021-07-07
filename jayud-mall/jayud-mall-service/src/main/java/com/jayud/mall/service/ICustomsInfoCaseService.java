package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BillCustomsInfoQueryForm;
import com.jayud.mall.model.bo.CreateCustomsInfoCaseForm;
import com.jayud.mall.model.po.CustomsInfoCase;
import com.jayud.mall.model.vo.BillCaseVO;

import java.util.List;

/**
 * <p>
 * 报关文件箱号 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface ICustomsInfoCaseService extends IService<CustomsInfoCase> {

    /**
     * 报关箱子-查询提单下未生成的订单箱子(分类型)
     * @param form
     * @return
     */
    List<BillCaseVO> findUnselectedBillCaseByCustoms(BillCustomsInfoQueryForm form);

    /**
     * 报关箱子-查询提单下已生成的订单箱子
     * @param form
     * @return
     */
    List<BillCaseVO> findSelectedBillCaseByCustoms(BillCustomsInfoQueryForm form);

    /**
     * 提单下的报关-生成报关清单
     * @param form
     */
    void createCustomsInfoCase(CreateCustomsInfoCaseForm form);
}
