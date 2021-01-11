package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.ProductClassify;
import com.jayud.oms.model.po.ServiceType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类型 服务类
 * </p>
 *
 * @author
 * @since 2020-09-15
 */
public interface IServiceTypeService extends IService<ServiceType> {


    List<ServiceType> getEnableParentServiceType(String code);
}
