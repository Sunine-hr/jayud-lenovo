package com.jayud.oauth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.bo.QueryLegalEntityForm;
import com.jayud.model.po.LegalEntity;
import com.jayud.model.vo.LegalEntityVO;

import java.util.List;
import java.util.Map;

public interface ILegalEntityService extends IService<LegalEntity> {

    IPage<LegalEntityVO> findLegalEntityByPage(QueryLegalEntityForm form);

    List<LegalEntityVO> findLegalEntity(Map<String,String> param);

}
