package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerRelationer;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户联系人表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface CustomerRelationerMapper extends BaseMapper<CustomerRelationer> {

    IPage<CustomerRelationerVO> findByPage(@Param("page") Page<CustomerRelationerVO> page, @Param("form")QueryCommonForm form);

    /**
     * 客户 客户下单人
     * @param customerId
     * @param stype
     * @return
     */
    List<CustomerRelationerVO> findCustomerRelationerByCustomerIdAndStype(@Param("customerId") Integer customerId, @Param("stype") String stype);
}
