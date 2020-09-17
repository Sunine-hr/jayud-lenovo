package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.service.IPortInfoService;
import com.jayud.oms.model.po.PortInfo;
import com.jayud.oms.mapper.PortInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 口岸基础信息 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-16
 */
@Service
public class PortInfoServiceImpl extends ServiceImpl<PortInfoMapper, PortInfo> implements IPortInfoService {

    @Override
    public List<PortInfo> findPortInfoByCondition(Map<String, Object> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","1");//有效的
        for(String key : param.keySet()){
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key,value);
        }
        return baseMapper.selectList(queryWrapper);
    }
}
