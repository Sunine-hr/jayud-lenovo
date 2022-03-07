package com.jayud.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.po.HsElements;
import com.jayud.auth.model.vo.CodeElementsVO;

/**
 * <p>
 * 申报要素表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IHsElementsService extends IService<HsElements> {

    IPage<CodeElementsVO> findElements(QueryForm form);
}
