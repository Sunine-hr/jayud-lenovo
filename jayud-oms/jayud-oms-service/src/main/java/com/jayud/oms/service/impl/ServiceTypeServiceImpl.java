package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.ServiceTypeMapper;
import com.jayud.oms.model.po.ServiceType;
import com.jayud.oms.service.IServiceTypeService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 服务类型 服务实现类
 * </p>
 *
 * @author
 * @since 2020-09-15
 */
@Service
public class ServiceTypeServiceImpl extends ServiceImpl<ServiceTypeMapper, ServiceType> implements IServiceTypeService {
    @Override
    public List<ServiceType> getEnableParentServiceType(String code) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",code);
        List<ServiceType> list = baseMapper.selectList(queryWrapper);
        return list;
    }
}
