package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.bo.SupplierServeForm;
import com.jayud.mall.model.bo.SupplierServeStatusForm;
import com.jayud.mall.model.po.SupplierServe;
import com.jayud.mall.model.vo.SupplierServeVO;

/**
 * <p>
 * 供应商服务 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
public interface ISupplierServeService extends IService<SupplierServe> {

    /**
     * 分页
     * @param form
     * @return
     */
    IPage<SupplierServeVO> findSupplierServeByPage(QuerySupplierServeForm form);

    /**
     * 保存供应商服务
     * @param form
     * @return
     */
    CommonResult saveSupplierServe(SupplierServeForm form);

    /**
     * 查询供应商服务
     * @param id
     * @return
     */
    SupplierServeVO findSupplierServeById(Long id);

    /**
     * 停用启用供应商服务
     * @param form
     */
    void disableEnabledSupplierServe(SupplierServeStatusForm form);
}
