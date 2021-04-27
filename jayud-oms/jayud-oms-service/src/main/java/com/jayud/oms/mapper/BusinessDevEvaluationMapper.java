package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.BusinessDevEvaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.BusinessDevEvaluationVO;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;

/**
 * <p>
 * 商业伙伴开发评估表 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-04-26
 */
public interface BusinessDevEvaluationMapper extends BaseMapper<BusinessDevEvaluation> {

    IPage<BusinessDevEvaluationVO> findByPage(Page<CustomsQuestionnaireVO> page, QueryCustomsQuestionnaireForm form);
}
