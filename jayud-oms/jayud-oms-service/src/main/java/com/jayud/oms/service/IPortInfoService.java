package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.PortInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 口岸基础信息 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-16
 */
public interface IPortInfoService extends IService<PortInfo> {

    /**
     * 根据条件获取通过口岸信息
     * @param param
     * @return
     */
    List<PortInfo> findPortInfoByCondition(Map<String,Object> param);

}
