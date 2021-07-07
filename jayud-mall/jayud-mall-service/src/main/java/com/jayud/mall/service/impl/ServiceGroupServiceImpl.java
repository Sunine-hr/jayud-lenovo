package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.ServiceGroupMapper;
import com.jayud.mall.model.bo.QueryServiceGroupForm;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;
import com.jayud.mall.model.vo.AccountPayableVO;
import com.jayud.mall.model.vo.ServiceGroupVO;
import com.jayud.mall.service.IServiceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public IPage<ServiceGroupVO> findServiceGroupByPage(QueryServiceGroupForm form) {
        //定义分页参数
        Page<ServiceGroupVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<ServiceGroupVO> pageInfo = serviceGroupMapper.findServiceGroupByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceGroupVO saveServiceGroup(ServiceGroupForm form) {
        ServiceGroup serviceGroup = ConvertUtil.convert(form, ServiceGroup.class);
        this.saveOrUpdate(serviceGroup);
        ServiceGroupVO serviceGroupVO = ConvertUtil.convert(serviceGroup, ServiceGroupVO.class);
        return serviceGroupVO;
    }

    @Override
    public ServiceGroupVO findServiceGroupById(Long id) {
        ServiceGroupVO serviceGroupVO = serviceGroupMapper.findServiceGroupById(id);
        return serviceGroupVO;
    }
}
