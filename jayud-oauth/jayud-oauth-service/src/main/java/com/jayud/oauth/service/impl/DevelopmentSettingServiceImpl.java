package com.jayud.oauth.service.impl;

import com.jayud.oauth.model.po.DevelopmentSetting;
import com.jayud.oauth.mapper.DevelopmentSettingMapper;
import com.jayud.oauth.service.IDevelopmentSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 开发设置表(仓库 和 供应商 创建之后自动生成开发配置) 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-23
 */
@Service
public class DevelopmentSettingServiceImpl extends ServiceImpl<DevelopmentSettingMapper, DevelopmentSetting> implements IDevelopmentSettingService {

}
