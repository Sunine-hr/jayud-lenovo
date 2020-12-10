package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryCustomerForm;
import com.jayud.mall.model.po.Customer;
import com.jayud.mall.model.vo.CustomerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<CustomerVO> findCustomerByPage(Page<CustomerVO> page, @Param("form") QueryCustomerForm form);
}
