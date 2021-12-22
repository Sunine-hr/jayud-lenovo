package com.jayud.tools.mapper;

import com.jayud.tools.model.po.FbaOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.jmx.export.annotation.ManagedNotifications;

/**
 * <p>
 * FBA订单 Mapper 接口
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Mapper
public interface FbaOrderMapper extends BaseMapper<FbaOrder> {

}
