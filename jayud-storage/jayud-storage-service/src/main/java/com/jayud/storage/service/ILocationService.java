package com.jayud.storage.service;

import com.jayud.storage.model.po.Location;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库位对应的库位编码 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
public interface ILocationService extends IService<Location> {

    List<Location> getList(Long id);

    boolean isExistence(String kuCode);
}
