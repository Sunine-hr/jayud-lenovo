package com.jayud.oceanship.service;

import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.model.po.Terms;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface ITermsService extends IService<Terms> {

    /**
     * 获取业务类型列表
     * @return
     */
    List<InitComboxVO> initTerms();
}
