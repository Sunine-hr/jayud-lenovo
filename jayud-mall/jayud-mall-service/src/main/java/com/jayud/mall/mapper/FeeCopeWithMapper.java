package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.FeeCopeWith;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提单(或柜子)对应费用明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-21
 */
@Mapper
@Component
public interface FeeCopeWithMapper extends BaseMapper<FeeCopeWith> {

}
