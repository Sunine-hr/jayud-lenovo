package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryPromoteCompanyForm;
import com.jayud.mall.model.po.PromoteCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.PromoteCompanyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 推广公司表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
@Mapper
public interface PromoteCompanyMapper extends BaseMapper<PromoteCompany> {

    IPage<PromoteCompanyVO> findPromoteCompanyByPage(Page<PromoteCompanyVO> page, @Param("form") QueryPromoteCompanyForm form);

    List<PromoteCompanyVO> findPromoteCompanyByParentId(@Param("parentId") Integer parentId);

    PromoteCompanyVO findPromoteCompanyByCompanyId(@Param("companyId") Integer companyId);
}
