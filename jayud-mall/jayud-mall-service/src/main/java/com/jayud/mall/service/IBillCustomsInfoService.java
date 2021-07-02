package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BillCustomsInfoQueryForm;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.model.vo.*;

import java.util.List;

/**
 * <p>
 * (提单)报关信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IBillCustomsInfoService extends IService<BillCustomsInfo> {

    /**
     * 根据报关信息id，查询提单对应报关箱号信息
     * @param b_id
     * @return
     */
    List<CustomsInfoCaseVO> findCustomsInfoCase(Long b_id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    BillCustomsInfoVO findBillCustomsInfoById(Long id);

    /**
     * 导出清单-报关箱子
     * @param b_id
     * @return
     */
    List<CustomsInfoCaseExcelVO> findCustomsInfoCaseBybid(Long b_id);

    /**
     * 导出报关清单文件（到商品一级）
     */
    CustomsListExcelVO findCustomsListExcelById(Long id);

    /**
     * 报关清单-查询关联的订单箱子以及订单报关文件(报关分为：买单 / 独立)
     * @param form
     * @return
     */
    List<OrderInfoVO> findSelectOrderInfoByCustoms(BillCustomsInfoQueryForm form);
}
