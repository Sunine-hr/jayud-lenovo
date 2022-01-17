package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BTaxClassCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BTaxClassCodeVO;
import com.jayud.scm.model.vo.HsCodeFormVO;

/**
 * <p>
 * 税务分类表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-29
 */
public interface IBTaxClassCodeService extends IService<BTaxClassCode> {

    IPage<BTaxClassCodeVO> findByPage(QueryForm form);
}
