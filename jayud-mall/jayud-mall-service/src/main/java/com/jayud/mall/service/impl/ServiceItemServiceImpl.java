package com.jayud.mall.service.impl;

import com.jayud.mall.model.bo.ServiceItemForm;
import com.jayud.mall.model.po.ServiceItem;
import com.jayud.mall.mapper.ServiceItemMapper;
import com.jayud.mall.model.vo.ServiceItemVO;
import com.jayud.mall.service.IServiceItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    ServiceItemMapper serviceItemMapper;

    @Override
    public List<ServiceItemVO> findServiceItem(ServiceItemForm form) {
        return serviceItemMapper.findServiceItem(form);
    }
}
