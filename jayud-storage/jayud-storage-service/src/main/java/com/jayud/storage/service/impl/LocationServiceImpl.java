package com.jayud.storage.service.impl;

import com.jayud.storage.model.po.Location;
import com.jayud.storage.mapper.LocationMapper;
import com.jayud.storage.service.ILocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库位对应的库位编码 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements ILocationService {

}
