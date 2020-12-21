package com.jayud.airfreight.service;

import com.jayud.airfreight.model.po.AirPort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.entity.InitComboxStrVO;

import java.util.List;

/**
 * <p>
 * 飞机港口地址表 服务类
 * </p>
 *
 * @author LDR
 * @since 2020-12-02
 */
public interface IAirPortService extends IService<AirPort> {

    /**
     * 模糊查询出发港和目的港
     */
    public List<AirPort> getLike(String portDeparture, String portDestination);

    /**
     * 下拉选项空运港口
     */
    public List<InitComboxStrVO> initAirPort();

    /**
     * 查询启用的空运港信息
     */
    public List<AirPort> getEnableAirPort();
}
