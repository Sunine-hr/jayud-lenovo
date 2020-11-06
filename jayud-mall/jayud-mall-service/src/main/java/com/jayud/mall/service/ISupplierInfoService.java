package com.jayud.mall.service;

import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.baomidou.mybatisplus.extension.service.IService;

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
    List<SupplierInfo> findSupplierInfo(SupplierInfoForm form);
}
