package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HsElements;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CodeElementsVO;

import java.util.List;

/**
 * <p>
 * 申报要素表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IHsElementsService extends IService<HsElements> {

    IPage<CodeElementsVO> findElements(QueryCommonForm form);
}
