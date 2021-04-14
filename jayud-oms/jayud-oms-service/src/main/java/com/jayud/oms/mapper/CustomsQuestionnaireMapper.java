package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 海关调查问卷 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-04-14
 */
@Mapper
public interface CustomsQuestionnaireMapper extends BaseMapper<CustomsQuestionnaire> {


    IPage<CustomsQuestionnaireVO> findByPage(Page<CustomsQuestionnaireVO> page, QueryCustomsQuestionnaireForm form);
}
