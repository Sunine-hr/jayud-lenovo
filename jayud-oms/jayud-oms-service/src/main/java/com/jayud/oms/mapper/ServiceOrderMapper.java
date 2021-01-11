package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.po.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 服务单信息表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2020-12-16
 */
@Mapper
public interface ServiceOrderMapper extends BaseMapper<ServiceOrder> {
}
