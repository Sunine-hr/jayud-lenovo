package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.po.AuditInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审核信息记录表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Mapper
public interface AuditInfoMapper extends BaseMapper<AuditInfo> {

    AuditInfo getLatestByExtId(@Param("id") Long id);
}
