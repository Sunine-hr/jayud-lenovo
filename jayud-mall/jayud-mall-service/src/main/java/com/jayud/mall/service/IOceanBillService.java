package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.OceanBillVO;

/**
 * <p>
 * 提单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOceanBillService extends IService<OceanBill> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form);

    /**
     * 保存提单信息
     * @param form
     */
    CommonResult<OceanBillVO> saveOceanBill(OceanBillForm form);

    /**
     * 查看提单详情
     * @param id
     * @return
     */
    CommonResult<OceanBillVO> lookOceanBill(Long id);

    /**
     * 提单-录入费用(根据 提单id 查询)
     * 1.查询提单详情
     * 2.查询提单费用明细
     * 3.查询提单对应的订单
     * @param id
     * @return
     */
    CommonResult<OceanBillVO> billLadingCost(Long id);
}
