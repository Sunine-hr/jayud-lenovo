package com.jayud.mall.service;

import com.jayud.mall.model.po.SupplierServiceType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.SupplierServiceTypeVO;

import java.util.List;

/**
 * <p>
 * 供应商服务类型 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
public interface ISupplierServiceTypeService extends IService<SupplierServiceType> {

    /**
     * 查询供应商服务类型
     * @return
     */
    List<SupplierServiceTypeVO> findSupplierServiceType();
}
