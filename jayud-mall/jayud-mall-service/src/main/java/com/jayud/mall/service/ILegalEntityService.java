package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.LegalEntityAuditForm;
import com.jayud.mall.model.bo.LegalEntityForm;
import com.jayud.mall.model.bo.QueryLegalEntityForm;
import com.jayud.mall.model.po.LegalEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.LegalEntityVO;

import java.util.List;

/**
 * <p>
 * 法人主体 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-11
 */
public interface ILegalEntityService extends IService<LegalEntity> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<LegalEntityVO> findLegalEntityPage(QueryLegalEntityForm form);

    /**
     * 根据id，查询
     * @param id
     * @return
     */
    LegalEntityVO findLegalEntityById(Long id);

    /**
     * 保存
     * @param form
     */
    void saveLegalEntity(LegalEntityForm form);

    /**
     * 审核
     * @param form
     */
    void auditLegalEntity(LegalEntityAuditForm form);

    /**
     * 查询list
     * @return
     */
    List<LegalEntityVO> findLegalEntity();
}
