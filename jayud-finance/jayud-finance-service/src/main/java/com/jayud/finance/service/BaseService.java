package com.jayud.finance.service;

import java.util.Optional;

/**
 * k3 基本数据查询 通用服务接口
 * @author bocong.zheng
 */
public interface BaseService {

    /**
     * K3 基本数据 列表查询
     * @param name 数据名称 支持模糊查询
     * @param clz 指定返回值类型
     * @return
     */
    <T> Object query(String name, Class<T> clz);


    /**
     * K3 基本数据 精确查询
     * @param name 数据名称
     * @param clz 指定返回值类型
     * @return
     */
    <T> Optional<T> get(String name, Class<T> clz);
}
