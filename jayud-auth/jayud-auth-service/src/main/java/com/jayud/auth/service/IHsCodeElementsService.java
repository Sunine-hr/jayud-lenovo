package com.jayud.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.po.HsCodeElements;
import com.jayud.auth.model.vo.HsCodeElementsVO;

import java.util.List;

/**
 * <p>
 * 海关税号要素明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IHsCodeElementsService extends IService<HsCodeElements> {

    boolean deleteCodeElementsByCodeNo(String codeNo);

    List<HsCodeElementsVO> getCodeElementsByCodeNo(String codeNo);
}
