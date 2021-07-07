package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.QueryServiceGroupForm;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;
import com.jayud.mall.model.vo.ServiceGroupVO;

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
    List<ServiceGroupVO> findServiceGroup(ServiceGroupForm form);

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<ServiceGroupVO> findServiceGroupByPage(QueryServiceGroupForm form);

    /**
     * 保存
     * @param form
     * @return
     */
    ServiceGroupVO saveServiceGroup(ServiceGroupForm form);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ServiceGroupVO findServiceGroupById(Long id);
}
