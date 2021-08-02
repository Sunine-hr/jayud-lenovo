package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCommodityFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CommodityFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CommodityFollowVO;

import java.util.List;

/**
 * <p>
 * 商品操作日志表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface ICommodityFollowService extends IService<CommodityFollow> {

    IPage<CommodityFollowVO> findListByCommodityId(QueryCommonForm form);

    boolean AddCommodityFollow(AddCommodityFollowForm followForm);
}
