package com.jayud.mall.service;

import com.jayud.mall.model.bo.ServiceItemForm;
import com.jayud.mall.model.po.ServiceItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ServiceItemVO;

import java.util.List;

/**
 * <p>
 * 基础表-服务项目表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
public interface IServiceItemService extends IService<ServiceItem> {

    /**
     * 查询 服务项目 list
     * @param form
     * @return
     */
    List<ServiceItemVO> findServiceItem(ServiceItemForm form);
}
