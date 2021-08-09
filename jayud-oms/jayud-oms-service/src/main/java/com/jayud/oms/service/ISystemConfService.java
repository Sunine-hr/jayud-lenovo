package com.jayud.oms.service;

import com.jayud.oms.model.po.SystemConf;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息系统配置 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-04
 */
public interface ISystemConfService extends IService<SystemConf> {

    List<SystemConf> getByCondition(SystemConf systemConf);
}
