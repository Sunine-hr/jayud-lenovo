package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerGuarantee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CustomerGuaranteeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 担保合同 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Mapper
public interface CustomerGuaranteeMapper extends BaseMapper<CustomerGuarantee> {

    IPage<CustomerGuaranteeVO> findByPage(@Param("page") Page<CustomerGuaranteeVO> page, @Param("form")QueryCommonForm form);
}
