package com.jayud.scm.mapper;

import com.jayud.scm.model.po.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
