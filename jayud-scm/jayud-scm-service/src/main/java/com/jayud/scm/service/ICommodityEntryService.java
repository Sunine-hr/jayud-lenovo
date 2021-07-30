package com.jayud.scm.service;

import com.jayud.scm.model.po.CommodityEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CommodityEntryVO;

import java.util.List;

/**
 * <p>
 * 商品申报要素明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface ICommodityEntryService extends IService<CommodityEntry> {

    List<CommodityEntryVO> getCommodityEntry(Integer id);

    boolean delete(Integer id);
}
