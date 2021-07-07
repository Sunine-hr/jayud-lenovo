package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BillClearanceInfoQueryForm;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseExcelVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import com.jayud.mall.model.vo.OrderInfoVO;

import java.util.List;

/**
 * <p>
 * (提单)清关信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IBillClearanceInfoService extends IService<BillClearanceInfo> {

    /**
     * 根据清关信息id，查询提单对应清关箱号信息
     * @param b_id
     * @return
     */
    List<ClearanceInfoCaseVO> findClearanceInfoCase(Long b_id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    BillClearanceInfoVO findBillClearanceInfoById(Long id);

    /**
     * 导出清单-清关箱子
     * @param b_id
     * @return
     */
    List<ClearanceInfoCaseExcelVO> findClearanceInfoCaseBybid(Long b_id);

    /**
     * 清关清单-查询关联的订单箱子以及订单清关文件(清关分为：买单 / 独立)
     * @param form
     * @return
     */
    List<OrderInfoVO> findSelectOrderInfoByClearance(BillClearanceInfoQueryForm form);
}
