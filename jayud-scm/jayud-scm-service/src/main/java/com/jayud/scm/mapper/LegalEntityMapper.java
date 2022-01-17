package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryLegalEntityForm;
import com.jayud.scm.model.po.LegalEntity;
import com.jayud.scm.model.vo.LegalEntityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LegalEntityMapper extends BaseMapper<LegalEntity> {

    IPage<LegalEntityVO> findLegalEntityByPage(Page page, @Param("form") QueryLegalEntityForm form);
}
