package com.jayud.mall.mapper;

import com.jayud.mall.model.po.AccountReceivable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 应收对账单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Mapper
@Component
public interface AccountReceivableMapper extends BaseMapper<AccountReceivable> {

}
