package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCreditVisitRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户走访记录-拜访人员(员工表)关联表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-02
 */
@Mapper
public interface CrmCreditVisitRoleMapper extends BaseMapper<CrmCreditVisitRole> {

    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-02
     * @param: page
     * @param: crmCreditVisitRole
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditVisitRole>
     **/
    IPage<CrmCreditVisitRole> pageList(@Param("page") Page<CrmCreditVisitRole> page, @Param("crmCreditVisitRole") CrmCreditVisitRole crmCreditVisitRole);

    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-02
     * @param: crmCreditVisitRole
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditVisitRole>
     **/
    List<CrmCreditVisitRole> list(@Param("crmCreditVisitRole") CrmCreditVisitRole crmCreditVisitRole);


    /**
     * @description 根据id物理删除
     * @author jayud
     * @date 2022-03-02
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author jayud
     * @date 2022-03-02
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id, @Param("username") String username);


    int updateCrmCreditVisitRole(@Param("crmCreditVisitRole") CrmCreditVisitRole crmCreditVisitRole);

}
