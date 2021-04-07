package com.jayud.oms.service;

import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.po.SupplierRelaLegal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商对应法人主体表 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-04-06
 */
public interface ISupplierRelaLegalService extends IService<SupplierRelaLegal> {

    /**
     * 保存法人主体与供应商对应关系
     * @param form
     * @return
     */
    Boolean saveSupplierRelLegal(AddSupplierInfoForm form);

    List<Long> getList(Long id);
}
