package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryCustomsFinanceCoRelationForm;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 云报关-财务金蝶中的供应商/客户公司名称对应关系表 Mapper 接口
 * </p>
 *
 * @author william.chen
 * @since 2020-09-24
 */
public interface CustomsFinanceCoRelationMapper extends BaseMapper<CustomsFinanceCoRelation> {

    /**
     * page
     * @param page
     * @param form
     * @return
     */
    IPage<CustomsFinanceCoRelation> findCompanyRelationPage(@Param("page") Page<CustomsFinanceCoRelation> page, @Param("form") QueryCustomsFinanceCoRelationForm form);
}
