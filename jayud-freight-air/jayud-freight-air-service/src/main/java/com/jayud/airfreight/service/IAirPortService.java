package com.jayud.airfreight.service;

import com.jayud.airfreight.model.po.AirPort;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
