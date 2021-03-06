package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.model.vo.SupplierInfoVO;
import com.jayud.mall.model.vo.SupplierServeVO;

import java.util.List;

/**
 * <p>
 * 供应商信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface ISupplierInfoService extends IService<SupplierInfo> {

    /**
     * 查询供应商信息list
     * @param form
     * @return
     */
    List<SupplierInfoVO> findSupplierInfo(QuerySupplierInfoForm form);

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form);

    /**
     * 保存供应商信息
     * @param form
     * @return
     */
    CommonResult saveSupplierInfo(SupplierInfoForm form);

    /**
     * 根据id，查询供应商详细
     * @param id
     * @return
     */
    CommonResult<SupplierInfoVO> findSupplierInfoById(Long id);

    /**
     * 启用供应商信息
     * @param id
     * @return
     */
    CommonResult<SupplierInfoVO> enableSupplierInfo(Long id);

    /**
     * 禁用供应商信息
     * @param id
     * @return
     */
    CommonResult<SupplierInfoVO> disableSupplierInfo(Long id);

    List<SupplierServeVO> findSupplierSerCostInfoById(Long supplierInfoId);
}
