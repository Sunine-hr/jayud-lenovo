package com.jayud.airfreight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.airfreight.mapper.AirPortMapper;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.po.AirPort;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.IAirPortService;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.InitComboxStrVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private IAirOrderService airOrderService;

    /**
     * 模糊查询出发港和目的港
     */
    @Override
    public List<AirPort> getLike(String portDeparture, String portDestination) {
        QueryWrapper<AirPort> condition = new QueryWrapper<>();
        if (StringUtils.isNotBlank(portDeparture)) {

        }

        return null;
    }

    @Override
    public List<InitComboxStrVO> initAirPort() {
        QueryWrapper<AirPort> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AirPort::getStatus, 1);
        List<AirPort> airPorts = this.list(queryWrapper);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (AirPort airPort : airPorts) {
            InitComboxStrVO initComboxVO = new InitComboxStrVO();
            initComboxVO.setCode(airPort.getCode());
            initComboxVO.setName(airPort.getName());
            initComboxStrVOS.add(initComboxVO);
        }
        return initComboxStrVOS;
    }

    /**
     * 查询启用的空运港信息
     */
    @Override
    public List<AirPort> getEnableAirPort() {
        QueryWrapper<AirPort> condition = new QueryWrapper<AirPort>();
        condition.lambda().eq(AirPort::getStatus, 1);
        return this.list(condition);
    }
}
