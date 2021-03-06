package com.jayud.oauth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.bo.QueryLegalEntityForm;
import com.jayud.oauth.model.po.LegalEntity;
import com.jayud.oauth.model.vo.LegalEntityVO;

import java.util.List;
import java.util.Map;

public interface ILegalEntityService extends IService<LegalEntity> {

    IPage<LegalEntityVO> findLegalEntityByPage(QueryLegalEntityForm form);

    List<LegalEntityVO> findLegalEntity(Map<String,String> param);

    LegalEntity getLegalEntityByLegalName(String name,Integer auditStatus);


    /**
     * 根据法人id集合配对code
     */
    Boolean matchingCodeByLegalIds(List<Long> legalEntityIds, String code);

    /**
     * 根据法人主体code获取法人信息
     * @param legalEntity
     * @return
     */
    List<LegalEntity> getByCondition(LegalEntity legalEntity);
}
