package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.ServiceGroupMapper;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;
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
    public List<ServiceGroup> findServiceGroup(ServiceGroupForm form) {
        QueryWrapper<ServiceGroup> queryWrapper = new QueryWrapper<>();
        String idCode = form.getIdCode();
        String codeName = form.getCodeName();
        String status = form.getStatus();
        if(idCode != null && idCode != ""){
            queryWrapper.like("id_code", idCode);
        }
        if(codeName != null && codeName != ""){
            queryWrapper.like("code_name", codeName);
        }
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<ServiceGroup> list = serviceGroupMapper.selectList(queryWrapper);
        return list;
    }
}
