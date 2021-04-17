package com.jayud.mall.mapper;

import com.jayud.mall.model.po.ActionItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 操作项 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Mapper
@Component
public interface ActionItemMapper extends BaseMapper<ActionItem> {

}
