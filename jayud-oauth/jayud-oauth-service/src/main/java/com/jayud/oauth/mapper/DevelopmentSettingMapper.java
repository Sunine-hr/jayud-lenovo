package com.jayud.oauth.mapper;

import com.jayud.oauth.model.po.DevelopmentSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 开发设置表(仓库 和 供应商 创建之后自动生成开发配置) Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-23
 */
@Mapper
public interface DevelopmentSettingMapper extends BaseMapper<DevelopmentSetting> {

}
