package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryLegalEntityForm;
import com.jayud.mall.model.po.LegalEntity;
import com.jayud.mall.model.vo.LegalEntityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 法人主体 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-11
 */
@Mapper
@Component
public interface LegalEntityMapper extends BaseMapper<LegalEntity> {

    /**
     * 分页查询
     * @param page 分页
     * @param form 查询条件
     * @return
     */
    IPage<LegalEntityVO> findLegalEntityPage(Page<LegalEntityVO> page, @Param("form") QueryLegalEntityForm form);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    LegalEntityVO findLegalEntityById(@Param("id") Long id);

    /**
     * 查询list
     * @return
     */
    List<LegalEntityVO> findLegalEntity();
}
