package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.ServiceItem;
import com.jayud.mall.mapper.ServiceItemMapper;
import com.jayud.mall.service.IServiceItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础表-服务项目表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Service
public class ServiceItemServiceImpl extends ServiceImpl<ServiceItemMapper, ServiceItem> implements IServiceItemService {

}
