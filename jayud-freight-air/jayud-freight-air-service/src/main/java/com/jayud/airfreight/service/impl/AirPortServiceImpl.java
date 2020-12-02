package com.jayud.airfreight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.mapper.AirPortMapper;
import com.jayud.airfreight.model.po.AirPort;
import com.jayud.airfreight.service.IAirPortService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 飞机港口地址表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2020-12-02
 */
@Service
public class AirPortServiceImpl extends ServiceImpl<AirPortMapper, AirPort> implements IAirPortService {

    /**
     * 模糊查询出发港和目的港
     */
    @Override
    public List<AirPort> getLike(String portDeparture, String portDestination) {
        QueryWrapper<AirPort> condition=new QueryWrapper<>();
        if (StringUtils.isNotBlank(portDeparture)){

        }

        return null;
    }
}
