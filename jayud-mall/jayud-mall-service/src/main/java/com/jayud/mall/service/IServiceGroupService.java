package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;

import java.util.List;

/**
 * <p>
 * 报价服务组 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IServiceGroupService extends IService<ServiceGroup> {

    /**
     * 查询报价服务组List
     */
    List<ServiceGroup> findServiceGroup(ServiceGroupForm form);

}
