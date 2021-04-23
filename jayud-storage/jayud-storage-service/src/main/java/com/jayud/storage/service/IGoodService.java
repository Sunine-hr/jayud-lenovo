package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryGoodForm;
import com.jayud.storage.model.po.Good;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.GoodVO;

import java.util.List;

/**
 * <p>
 * 商品信息维护表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-22
 */
public interface IGoodService extends IService<Good> {

    IPage<GoodVO> findGoodsByPage(QueryGoodForm form);

    List<GoodVO> getList(QueryGoodForm form);

    boolean isCommodity(String sku);
}
