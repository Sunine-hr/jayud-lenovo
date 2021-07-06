package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.BillCustomsCounterList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * （提单)报关、清关 关联 柜子清单 表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-07-06
 */
@Mapper
@Component
public interface BillCustomsCounterListMapper extends BaseMapper<BillCustomsCounterList> {

}
