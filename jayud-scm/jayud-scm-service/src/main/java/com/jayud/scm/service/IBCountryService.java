package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCountryForm;
import com.jayud.scm.model.po.BCountry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.HsCodeFormVO;

import java.util.List;

/**
 * <p>
 * 国家表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IBCountryService extends IService<BCountry> {

    IPage<BCountryVO> findCountryList(QueryCountryForm form);
}
