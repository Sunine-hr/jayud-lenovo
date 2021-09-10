package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BBanks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BBanksVO;

/**
 * <p>
 * 公司银行账户 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
public interface IBBanksService extends IService<BBanks> {

    IPage<BBanksVO> findByPage(QueryForm form);
}
