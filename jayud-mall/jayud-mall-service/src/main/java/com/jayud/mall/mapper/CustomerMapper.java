package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface CustomerMapper extends BaseMapper<Customer> {

}
