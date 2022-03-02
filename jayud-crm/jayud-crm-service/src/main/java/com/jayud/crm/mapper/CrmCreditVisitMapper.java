package com.jayud.crm.mapper;

import com.jayud.crm.model.bo.CrmCreditVisitForm;
import com.jayud.crm.model.po.CrmCreditVisit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.jayud.crm.model.vo.CrmCreditVisitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_客户走访记录 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-02
 */
@Mapper
public interface CrmCreditVisitMapper extends BaseMapper<CrmCreditVisit> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: page
     * @param: crmCreditVisit
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditVisit>
     **/
    IPage<CrmCreditVisitVO> pageList(@Param("page") Page<CrmCreditVisitForm> page, @Param("crmCreditVisit") CrmCreditVisitForm crmCreditVisit);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisit
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditVisit>
     **/
    List<CrmCreditVisit> list(@Param("crmCreditVisit") CrmCreditVisit crmCreditVisit);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);
}
