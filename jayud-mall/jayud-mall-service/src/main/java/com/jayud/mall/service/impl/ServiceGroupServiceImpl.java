package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.ServiceGroupMapper;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;
import com.jayud.mall.model.vo.ServiceGroupVO;
import com.jayud.mall.service.IServiceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报价服务组 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class ServiceGroupServiceImpl extends ServiceImpl<ServiceGroupMapper, ServiceGroup> implements IServiceGroupService {

    @Autowired
    ServiceGroupMapper serviceGroupMapper;

    @Override
    public List<ServiceGroupVO> findServiceGroup(ServiceGroupForm form) {
        List<ServiceGroupVO> list = serviceGroupMapper.findServiceGroup(form);
        return list;
    }
}
