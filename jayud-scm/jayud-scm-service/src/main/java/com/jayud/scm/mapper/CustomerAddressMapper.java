package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CustomerAddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户常用地址表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {

    IPage<CustomerAddressVO> findByPage(@Param("page") Page<CustomerAddressVO> page, @Param("form")QueryCommonForm form);
}
