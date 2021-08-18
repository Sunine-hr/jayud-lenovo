package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerMaintenanceSetup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CustomerMaintenanceSetupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户维护人表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-07
 */
@Mapper
public interface CustomerMaintenanceSetupMapper extends BaseMapper<CustomerMaintenanceSetup> {

    IPage<CustomerMaintenanceSetupVO> findByPage(@Param("page") Page<CustomerMaintenanceSetupVO> page, @Param("form") QueryCommonForm form);

    /**
     * 客户 的 `商务员`、`业务员`
     * @param customerId
     * @param roleName
     * @return
     */
    List<CustomerMaintenanceSetupVO> findCustomerMaintenanceSetupBycustomerIdAndRoleName(@Param("customerId") Integer customerId, @Param("roleName") String roleName);
}
