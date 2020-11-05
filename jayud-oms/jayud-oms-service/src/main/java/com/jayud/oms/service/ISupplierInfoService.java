package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.func.SFunction;
import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.bo.QueryAuditSupplierInfoForm;
import com.jayud.oms.model.bo.QuerySupplierInfoForm;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.SupplierInfoVO;

import java.util.List;

/**
 * <p>
 * 供应商信息 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
public interface ISupplierInfoService extends IService<SupplierInfo> {

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form);

    /**
     * 新增编辑供应商
     *
     * @param form
     * @return
     */
    boolean saveOrUpdateSupplierInfo(AddSupplierInfoForm form);

    /**
     * 分页查询供应商审核信息
     */
    IPage<SupplierInfoVO> findAuditSupplierInfoByPage(QueryAuditSupplierInfoForm form);

    /**
     * 获取启用审核通过供应商
     */
    List<SupplierInfo> getApprovedSupplier(String... fields);
}
