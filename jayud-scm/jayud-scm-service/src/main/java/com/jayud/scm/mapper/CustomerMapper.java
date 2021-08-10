package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCustomerForm;
import com.jayud.scm.model.po.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CommodityFormVO;
import com.jayud.scm.model.vo.CustomerFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    IPage<CustomerFormVO> findByPage(@Param("page") Page<CommodityFormVO> page, @Param("form") QueryCustomerForm form);

    void toExamine(Map<String, Object> map);

    void deApproval(Map<String, Object> map);
}
