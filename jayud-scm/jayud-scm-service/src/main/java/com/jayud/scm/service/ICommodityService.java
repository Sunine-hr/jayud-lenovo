package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.Commodity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CommodityDetailVO;
import com.jayud.scm.model.vo.CommodityFormVO;
import com.jayud.scm.model.vo.CommodityVO;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
public interface ICommodityService extends IService<Commodity> {

    boolean delete(DeleteForm deleteForm);

    IPage<CommodityFormVO> findByPage(QueryCommodityForm commodityForm);

    boolean saveOrUpdateCommodity(AddCommodityForm form);

    CommodityVO findCommodityById(Integer id);

    CommodityDetailVO findCommodityDetailById(Integer id);

    boolean taxClassification(TaxCommodityForm form);

    boolean reviewCommodity(AddReviewCommodityForm form);

    boolean reviewCommodities(AddReviewCommodityForm form);

    boolean addCommodity(List<AddCommodityModelForm> list);
}
