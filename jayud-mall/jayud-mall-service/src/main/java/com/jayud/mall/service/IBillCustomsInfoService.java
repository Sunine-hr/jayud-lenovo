package com.jayud.mall.service;

import com.jayud.mall.model.po.BillCustomsInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import com.jayud.mall.model.vo.CustomsInfoCaseExcelVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;

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
}
