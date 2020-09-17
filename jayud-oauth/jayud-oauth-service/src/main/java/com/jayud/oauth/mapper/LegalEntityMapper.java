package com.jayud.oauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oauth.model.bo.QueryLegalEntityForm;
import com.jayud.oauth.model.po.LegalEntity;
import com.jayud.oauth.model.vo.LegalEntityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LegalEntityMapper extends BaseMapper<LegalEntity> {

    IPage<LegalEntityVO> findLegalEntityByPage(Page page, @Param("form") QueryLegalEntityForm form);
}
