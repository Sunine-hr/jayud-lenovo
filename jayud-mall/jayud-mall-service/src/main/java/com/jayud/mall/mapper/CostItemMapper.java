package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CostItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 费用项目 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-28
 */
@Mapper
@Component
public interface CostItemMapper extends BaseMapper<CostItem> {

}
